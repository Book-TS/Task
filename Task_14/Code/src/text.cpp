// #include <Arduino.h>
// #include <WebServer.h>
// #include <ArduinoJson.h>

// // Thông tin kết nối Wifi
// const char *SSID = "BookTS";
// const char *PWD = "12345678";

// // Khai báo đối tượng webserver
// WebServer server(80);

// // Khai báo các
// StaticJsonDocument<250> jsonDocument;

// char buffer[250];

// // Khai báo các biến chứa dữ liệu test
// float data1 = 0;
// float data2 = 0;
// float data3 = 0;

// void setup_routing();                           // Khai báo các tài nguyên trên Server
// void create_json(char *tag, float value);       // Tạo đối tượng JSON
// void add_json_object(char *tag, float value);   // Tách chuỗi JSON
// void read_all_data(void *parameter);            // Random các giá trị cảm biến
// void blinking_led(void *parameter);             // Chớp tắt LED
// void getData1();                                // Đọc data 1
// void getData2();                                // Đọc data 2
// void getData3();                                // Đọc data 3
// void getAllData();                              // Đọc cả 3 data
// void handlePost();                              // Đọc dữ liệu control
// void setup_task();                              // Khởi động tác vụ

// void setup()
// {
//   Serial.begin(115200);

//   // Chờ kết nối wifi
//   Serial.print("Connecting to Wi-Fi");
//   WiFi.begin(SSID, PWD);
//   while (WiFi.status() != WL_CONNECTED)
//   {
//     Serial.print(".");
//     delay(500);
//   }
//   Serial.print("Connected! IP Address: ");
//   Serial.println(WiFi.localIP());
  
//   // Tạo task và các tài nguyên trên webserver
//   setup_task();
//   setup_routing();
//   pinMode(2, OUTPUT);
// }

// // Vòng lập chỉnh chờ yêu cầu từ client
// void loop()
// {
//   server.handleClient();
//   getData1();
//   delay(1000);
// }


// void setup_routing() {
//   server.on("/data1", getData1);
//   server.on("/data2", getData2);
//   server.on("/data3", getData3);
//   server.on("/data", getAllData);
//   server.on("/control", HTTP_POST, handlePost);
//   server.begin();
// }

// void create_json(char *tag, float value) {
//   jsonDocument.clear();
//   jsonDocument["type"] = tag;
//   jsonDocument["value"] = value;
//   serializeJson(jsonDocument, buffer);
// }

// void add_json_object(char *tag, float value) {
//   JsonObject obj = jsonDocument.createNestedObject();
//   obj["type"] = tag;
//   obj["value"] = value;
// }

// void read_all_data(void *parameter) {
//   int i;
//   for (;;) {
//     data1 = random(0, 255);
//     data2 = random(0, 255);
//     data3 = random(0, 255);
//     Serial.println("Read all data");
//     vTaskDelay(5000 / portTICK_PERIOD_MS);
//   }
// }

// void blinking_led(void *parameter) {
//   int i;
//   for (;;) {
//     digitalWrite(2, 1);
//     vTaskDelay(1000 / portTICK_PERIOD_MS);
//     digitalWrite(2, 0);
//     vTaskDelay(1000 / portTICK_PERIOD_MS);
//   }
// }

// void getData1() {
//   Serial.println("Get data 1");
//   create_json("Data1", data1);
//   server.send(200, "application/json", buffer);
// }

// void getData2() {
//   Serial.println("Get data 2");
//   create_json("Data2", data2);
//   server.send(200, "application/json", buffer);
// }

// void getData3() {
//   Serial.println("Get data 3");
//   create_json("Data3", data3);
//   server.send(200, "application/json", buffer);
// }

// void getAllData() {
//   Serial.println("Get all data");
//   jsonDocument.clear();
//   add_json_object("Data1", data1);
//   add_json_object("Data2", data3);
//   add_json_object("Data3", data3);
//   serializeJson(jsonDocument, buffer);
//   server.send(200, "application/json", buffer);
// }

// void handlePost() {
//   if (server.hasArg("plain") == false) {}
  
//   // Đọc nội dung yêu cầu post
//   String body = server.arg("plain");
//   deserializeJson(jsonDocument, body);
//   // In ra màn hình để kiểm tra
//   Serial.println(server.arg(1));
//   Serial.println(body);
  
//   // Đọc các giá trị từ server
//   uint8_t control1 = jsonDocument["control1 "];
//   uint8_t control2 = jsonDocument["control2 "];
//   uint8_t control3 = jsonDocument["control3 "];
  
//   // In giá trị để kiểm tra
//   Serial.println("control1 = " + String(control1));
//   Serial.println("control2 = " + String(control2));
//   Serial.println("control3 = " + String(control3));

//   server.send(200, "application/json", "{}");
// }

// void setup_task() {
//   xTaskCreate(
//       read_all_data, "Read all data",
//       1000, NULL, 1, NULL);
//   xTaskCreate(
//       blinking_led, "Blinking led",
//       1000, NULL, 1, NULL);
// }