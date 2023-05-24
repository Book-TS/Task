// #include <Arduino.h>
// #include <myNTPClient.h>
// #include <myFirebaseESP32.h>
// #include <myDS18B20.h>
// #include <myHC_SR04.h>

// #define bt_temp 16
// #define bt_wl 17
// #define relay_1 12
// #define relay_2 14
// #define led_status 26
// #define led_connect 27

// myNTPClient mNTPClient;
// myFirebaseESP32 mFirebaseESP32;
// myDS18B20 mDS18B20;
// myHC_SR04 mHC_SR04;

// int timeOld_1 = 0;
// int timeOld_2 = 0;
// int timeOld_3 = 0;
// int timeOld_4 = 0;

// void setup()
// {
//     Serial.begin(115200);

//     mFirebaseESP32.init();

//     mNTPClient.init();

//     mDS18B20.init();

//     mHC_SR04.init();

//     pinMode(relay_1, OUTPUT);
//     pinMode(relay_2, OUTPUT);
//     pinMode(led_status, OUTPUT);
//     pinMode(led_connect, OUTPUT);
//     pinMode(bt_temp, INPUT);
//     pinMode(bt_wl, INPUT);
// }

// void loop()
// {
//     if ((unsigned long)(millis() - timeOld_1) > 200)
//     {
//         Serial.println("Lay du lieu firebase");
//         int status_1 = mFirebaseESP32.readInt("giatri/relay_1");
//         vTaskDelay(50 / portTICK_PERIOD_MS);
//         digitalWrite(relay_1, status_1);

//         int status_2 = mFirebaseESP32.readInt("giatri/relay_2");
//         vTaskDelay(50 / portTICK_PERIOD_MS);
//         digitalWrite(relay_2, status_2);

//         timeOld_1 = millis();
//     }

//     if ((unsigned long)(millis() - timeOld_2) > 200)
//     {
//         int temp_1 = mFirebaseESP32.readInt("giatri/doc_1");
//         if (temp_1)
//         {
//             mFirebaseESP32.putString("giatri/thoigian", mNTPClient.getTimeandDate());
//             mFirebaseESP32.putInt("giatri/nhietdo", mDS18B20.readDS18());
//             mFirebaseESP32.putInt("giatri/doc_1", 0);
//         }

//         int temp_2 = mFirebaseESP32.readInt("giatri/doc_2");
//         if (temp_2)
//         {
//             mFirebaseESP32.putString("giatri/thoigian", mNTPClient.getTimeandDate());
//             mFirebaseESP32.putInt("giatri/mucnuoc",  int(50 - mHC_SR04.readDistance()));
//             mFirebaseESP32.putInt("giatri/doc_2", 0);
//         }

//         timeOld_2 = millis();
//     }

//     if ((unsigned long)(millis() - timeOld_3) > 200)
//     {
//         if (digitalRead(bt_temp) == 0)
//         {
//             mFirebaseESP32.putString("giatri/thoigian", mNTPClient.getTimeandDate());
//             mFirebaseESP32.putInt("giatri/nhietdo", mDS18B20.readDS18());
//             mFirebaseESP32.putInt("giatri/temp",  1);
//             while (!digitalRead(bt_temp));
//         }

//         if (digitalRead(bt_wl) == 0)
//         {
//             mFirebaseESP32.putString("giatri/thoigian", mNTPClient.getTimeandDate());
//             mFirebaseESP32.putInt("giatri/mucnuoc",  int(50 - mHC_SR04.readDistance()));
//             mFirebaseESP32.putInt("giatri/wl",  1);
//             while (!digitalRead(bt_wl));
//         }

//         timeOld_3 = millis();
//     }

//     if ((unsigned long)(millis() - timeOld_4) > 10000)
//     {
//         Serial.println("Dua du lieu len firebase");
//         mFirebaseESP32.putString("giatri/thoigian", mNTPClient.getTimeandDate());
//         mFirebaseESP32.putInt("giatri/nhietdo", mDS18B20.readDS18());
//         mFirebaseESP32.putInt("giatri/mucnuoc", int(50 - mHC_SR04.readDistance()));

//         timeOld_4 = millis();
//     }
// }

#include <Arduino.h>
#include <FirebaseESP32.h>
#include <ArduinoJson.h>
#include <myDS18B20.h>
#include <myHC_SR04.h>
#include <myNTPClient.h>
#include <LiquidCrystal_I2C.h>

#define FIREBASE_HOST "https://iot-ho-ca-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "xHgRCShIShva0WPy8Vq8nz4BojJMU14yZopNUQnx"

#define WIFI_SSID "BookTS"
#define WIFI_PASSWORD "12345678"

#define bt_temp 16
#define bt_wl 17
#define relay1 12
#define relay2 14
#define led_status 5
#define led_connect 15

struct sendData
{
    int nhietdo;
    int mucnuoc;
    String thoigian;
};

struct receiveData
{
    int doc_1;
    int doc_2;
    int relay_1;
    int relay_2;
};

byte degree[8] = {
    0B01110,
    0B01010,
    0B01110,
    0B00000,
    0B00000,
    0B00000,
    0B00000,
    0B00000};

int timeOld_1 = 0;
int timeOld_2 = 0;
int timeOld_3 = 0;
int timeOld_4 = 0;
int time_1 = 500;
int time_2 = 10000;
int time_3 = 500;
int time_4 = 200;

FirebaseData fbdt;
sendData data;
receiveData rcvData;
myDS18B20 mDS18B20;
myHC_SR04 mHC_SR04;
myNTPClient mNTPClient;
LiquidCrystal_I2C mLCD(0x27, 16, 2);

void configIO();
void configFirebase();
void sendValue();
void receiveValue();
void ledStatus();

void setup()
{
    Serial.begin(115200);
    configIO();
    configFirebase();
    mDS18B20.init();
    mHC_SR04.init();
    mNTPClient.init();

    mLCD.init();
    mLCD.backlight();
    mLCD.setCursor(2, 0);
    mLCD.print("Aquarium IoT");
    mLCD.createChar(1, degree);
}

void loop()
{
    if ((unsigned long)(millis() - timeOld_1) > time_1)
    {
        Serial.println("Receive Value");
        receiveValue();
        timeOld_1 = millis();
    }
    if ((unsigned long)(millis() - timeOld_2) > time_2)
    {
        Serial.println("Send Data");
        sendValue();

        timeOld_2 = millis();
    }
    if ((unsigned long)(millis() - timeOld_3) > time_3)
    {
        Serial.println("Update device");
        digitalWrite(relay1, rcvData.relay_1);
        digitalWrite(relay2, rcvData.relay_2);
        timeOld_3 = millis();
    }
    if ((unsigned long)(millis() - timeOld_4) > time_4)
    {
        if (digitalRead(bt_temp) == 0)
        {
            Firebase.setInt(fbdt, "/giatri/nhietdo", mDS18B20.readDS18());
            delay(50);
            Firebase.setString(fbdt, "/giatri/thoigian", mNTPClient.getTimeandDate());
            ledStatus();
            while (!digitalRead(bt_temp))
                ;
        }

        if (digitalRead(bt_wl) == 0)
        {
            Firebase.setInt(fbdt, "/giatri/mucnuoc", mHC_SR04.readDistance());
            delay(50);
            Firebase.setString(fbdt, "/giatri/thoigian", mNTPClient.getTimeandDate());
            ledStatus();
            while (!digitalRead(bt_wl))
                ;
        }
        timeOld_4 = millis();
    }
}

void configIO()
{
    pinMode(led_status, OUTPUT);
    pinMode(led_connect, OUTPUT);
    pinMode(relay1, OUTPUT);
    pinMode(relay2, OUTPUT);

    digitalWrite(led_status, LOW);
    digitalWrite(led_connect, LOW);
    digitalWrite(relay1, LOW);
    digitalWrite(relay2, LOW);
}

void configFirebase()
{
    // Cấu hình wifi
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print(".");
        delay(300);
    }

    digitalWrite(led_connect, HIGH);
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());
    Serial.println();

    // Cấu hình firebase
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    Firebase.reconnectWiFi(true);
}

void sendValue()
{
    data.nhietdo = mDS18B20.readDS18();
    data.mucnuoc = mHC_SR04.readDistance();
    data.thoigian = mNTPClient.getTimeandDate();

    mLCD.setCursor(1, 1);
    mLCD.print("T:");
    mLCD.print(data.nhietdo);
    mLCD.write(1);
    mLCD.print("C");

    mLCD.setCursor(8, 1);
    mLCD.print("WL:");
    mLCD.print(data.mucnuoc);
    mLCD.print("cm ");

    FirebaseJson json;
    json.add("nhietdo", data.nhietdo);
    json.add("mucnuoc", data.mucnuoc);
    json.add("thoigian", data.thoigian);
    if (Firebase.ready())
    {
        if (Firebase.setJSON(fbdt, "giatri", json))
        {
            ledStatus();
        }
        else
        {
            Serial.println(fbdt.errorReason());
        }
    }
    vTaskDelay(1000 / portTICK_PERIOD_MS);
}

void receiveValue()
{
    if (Firebase.getJSON(fbdt, "/app"))
    {
        if (fbdt.dataType() == "json")
        {
            StaticJsonDocument<200> jsonDoc;
            DeserializationError error = deserializeJson(jsonDoc, fbdt.jsonString());

            if (error)
            {
                Serial.print("Parsing JSON failed: ");
                Serial.println(error.c_str());
                return;
            }
            rcvData.doc_1 = jsonDoc["doc_1"];
            rcvData.doc_2 = jsonDoc["doc_2"];
            rcvData.relay_1 = jsonDoc["relay_1"];
            rcvData.relay_2 = jsonDoc["relay_2"];
        }
        else
        {
            Serial.println("Invalid data type received from Firebase");
        }
    }
    else
    {
        Serial.println("Failed to get data from Firebase");
    }
}

void ledStatus()
{
    for (int i = 0; i < 10; i++)
    {
        digitalWrite(led_status, HIGH);
        delay(100);
        digitalWrite(led_status, LOW);
        delay(100);
    }
}