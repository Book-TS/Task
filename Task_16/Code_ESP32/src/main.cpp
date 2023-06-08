#include <Arduino.h>
#include <FirebaseESP32.h>

#define WIFI_SSID "BookTS"
#define WIFI_PASSWORD "12345678"

#define FIREBASE_HOST "https://dieu-khien-nhiet-do-dixell-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "9xBA2JsMxk8yUzlBkHF39q2n0qmynDykaERBBWKq"

FirebaseData fbdt;
FirebaseJson json;

int count = 0;

void configWifi();
void configFirebase();
void putInt(String path, int data);

void setup()
{
  Serial.begin(9600);
  configWifi();
  configFirebase();
}

void loop()
{
  putInt("test", count);
  count++;
  delay(1000);
}

void configWifi()
{
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
}

void configFirebase()
{
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
}

void putInt(String path, int data) {
  Firebase.setInt(fbdt, path, data);
  Serial.println("put Int done!");
}