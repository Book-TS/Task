#include <Arduino.h>
#include <WebServer.h>
#include <ArduinoJson.h>
#include <myDS18B20.h>

#define trig 17
#define echo 16
#define a1 5
#define in1 14
#define in2 12

// Thông tin kết nối Wifi
const char *SSID = "ten_wifi";
const char *PWD = "passowrd";

// Khai báo đối tượng webserver
WebServer server(80);

// Khai báo
myDS18B20 mDS18B20;
StaticJsonDocument<250> jsonDocument;

char buffer[250];

// Khai bao bien
int temp = 0;
int turbidity = 0;
int wl = 0;
int pump = 0;

void setup_routing();                         // Khai báo các tài nguyên trên Server
void add_json_object(char *tag, float value); // Tách chuỗi JSON
void read_all_data(void *parameter);          // Random các giá trị cảm biến
void getAllData();                            // Đọc cả 3 data
void handlePost();                            // Đọc dữ liệu control
void setup_task();                            // Khởi động tác vụ
int readDistance();                           // Đọc khoản cách
int readTurbidity();                          // Đọc độ đục
void pumpCtrl(int sta);                       // Điều khiển bơm

void setup()
{
  Serial.begin(115200);

  // Chờ kết nối wifi
  Serial.print("Connecting to Wi-Fi");
  WiFi.begin(SSID, PWD);
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(500);
  }
  Serial.print("Connected! IP Address: ");
  Serial.println(WiFi.localIP());

  // Tạo task và các tài nguyên trên webserver
  setup_task();
  setup_routing();

  // Cấu hình biến trở
  pinMode(INPUT, a1);
  // Cấu hình cảm biến siêu âm
  pinMode(INPUT, echo);
  pinMode(OUTPUT, trig);
  // Cấu hình L298N
  pinMode(OUTPUT, in1);
  pinMode(OUTPUT, in2);

  // Cấu hình cảm biến nhiệt độ
  mDS18B20.init();
}

// Vòng lập chỉnh chờ yêu cầu từ client
void loop()
{
  server.handleClient();

  // Kiểm tra độ đục và mức để để điều khiển động cơ
  if(turbidity > 500) pumpCtrl(2);
  else if (turbidity < 200) pumpCtrl(0);
  else if (wl < 5) pumpCtrl(1);
  delay(500);
}

void setup_routing()
{
  server.on("/data", getAllData);
  server.on("/control", HTTP_POST, handlePost);
  server.begin();
}

void add_json_object(char *tag, float value)
{
  JsonObject obj = jsonDocument.createNestedObject();
  obj["type"] = tag;
  obj["value"] = value;
}

void read_all_data(void *parameter)
{ 
  temp = mDS18B20.readDS18();           // Đọc cảm biến nhiệt độ
  turbidity = readTurbidity();          // Đọc biến trở rồi chuyển thành độ đục
  wl = readDistance();                  // Đọc mức nước
}

void getAllData()
{
  Serial.println("Get all data");
  jsonDocument.clear();
  add_json_object("data1", temp);                 // Thêm dữ liệu nhiệt độ vào chuỗi json
  add_json_object("data2", turbidity);            // Thêm dữ liệu độ đục vào chuỗi json
  add_json_object("data3", wl);                   // Thêm dữ liệu mức nước vào chuỗi json

  serializeJson(jsonDocument, buffer);            // Chuyển thành chuỗi json
  server.send(200, "application/json", buffer);   // Gửi chuỗi json vừa chuyển lên server
}

void handlePost()
{
  if (server.hasArg("plain") == false)
  {
  }

  // Đọc nội dung yêu cầu post
  String body = server.arg("plain");
  deserializeJson(jsonDocument, body);
  // In ra màn hình để kiểm tra
  Serial.println(server.arg(1));
  Serial.println(body);

  // Đọc các giá trị từ server
  pump = jsonDocument["pump1"];
  
  // In giá trị để kiểm tra
  Serial.println("control1 = " + String(pump));

  // Lấy giá trị đọc về để điều khiển máy bơm
  pumpCtrl(pump);       

  // Phản hồi về server
  server.send(200, "application/json", "{}");
}

void setup_task()
{
  // Tạo tác vụ đọc cảm biến mỗi 1s
  xTaskCreate(
      read_all_data, "Read all data",
      1000, NULL, 1, NULL);
}

int readDistance()
{
  digitalWrite(trig, 0);        // Tắt chân trig
  delayMicroseconds(2);         // Chờ 2 micro giây
  digitalWrite(trig, 1);        // Bật chân trig
  delayMicroseconds(10);        // Chờ 10 micro giây
  digitalWrite(trig, 0);        // Tắt chân trig

  unsigned long duration = pulseIn(echo, HIGH);       // Đọc độ rộng xung

  int distance = int(duration / 2 / 29.412);          // Tính toàn khoảng cách
  return (20 - distance);                             // Vì cảm biến đặt cách đáy 20cm nên ta lấy 20cm - khoảng cách tới mặt nước ta được mức nước hiện tại
}

int readTurbidity() {
  return (map(analogRead(a1), 0, 1000, 0, 1023));       // Đọc analog ròi chuyển sang độ đục
}

void pumpCtrl(int sta) {
  if(sta == 0) {
    // Tắt bơm
    digitalWrite(in1, LOW);
    digitalWrite(in2, LOW);
  }
  else if(sta == 1 ) {
    // Bơm vào
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
  }
  else if (sta == 2) {
    // Bơm ra
    digitalWrite(in1, LOW);
    digitalWrite(in2, HIGH);
  }
}