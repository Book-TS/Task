#include "myFirebaseESP32.h"
#include <Arduino.h>
#include <addons/RTDBHelper.h>
#include <FirebaseESP32.h>

FirebaseData fbdt;
FirebaseJson json;

myFirebaseESP32::myFirebaseESP32()
{
  ;
}

void myFirebaseESP32::init()
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

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
}

void myFirebaseESP32::putInt(String path, int data)
{
  Firebase.setInt(fbdt, path, data);
  Serial.println("put Int done!");
}

void myFirebaseESP32::putFloat(String path, float data)
{
  Firebase.setFloat(fbdt, path, data);
  Serial.println("put float done!");
}

void myFirebaseESP32::putString(String path, String data)
{
  Firebase.setString(fbdt, path, data);
  Serial.println("put String done!");
}

int myFirebaseESP32::readInt(String path)
{
  int intValue = 0;
  if (Firebase.RTDB.getInt(&fbdt, path))
  {
    if (fbdt.dataType() == "int")
    {
      intValue = fbdt.intData();
    }
  }
  return intValue;
}

float myFirebaseESP32::readFloat(String path)
{
  Firebase.getFloat(fbdt, path);
  return fbdt.floatData();
}

String myFirebaseESP32::readString(String path)
{
  Firebase.getString(fbdt, path);
  return fbdt.stringData();
}
