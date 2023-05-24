#ifndef myFirebaseESP32_h
#define myFirebaseESP32_h

#include <Arduino.h>
#include <FirebaseESP32.h>
#include <ArduinoJson.h>

class myFirebaseESP32
{
  public:
    myFirebaseESP32();

    void init();
    void putInt(String path, int data);
    void putFloat(String path, float data);
    void putString(String path, String data);
    int readInt(String path);
    float readFloat(String path);
    String readString(String path);

  private:
    #define FIREBASE_HOST "https://iot-ho-ca-default-rtdb.firebaseio.com/"
    #define FIREBASE_AUTH "xHgRCShIShva0WPy8Vq8nz4BojJMU14yZopNUQnx"

    #define WIFI_SSID "BookTS"
    #define WIFI_PASSWORD "12345678"
};
#endif