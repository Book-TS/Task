#include <Arduino.h>
#include <LiquidCrystal_I2C.h>
#include <Arduino_FreeRTOS.h>

#define bt_start  2
#define bt_stop   3
#define bt_up     4
#define bt_down   5
#define relay_1   6
#define relay_2   7
#define sensor    8

int val = 100;
int status = 0;

LiquidCrystal_I2C mLCD(0x27, 16, 2);

void initGPIO();
void pumpRun(int ml);

void setup() {
  Serial.begin(9600);
  initGPIO();

  mLCD.init();
  mLCD.backlight();
  mLCD.setCursor(0, 0);
  mLCD.print("Init Done");
} 

void loop() {
  if(digitalRead(bt_start) == 0) {
    digitalWrite(relay_1, HIGH);
    status = 1;
    mLCD.clear();
    mLCD.setCursor(0, 0);
    mLCD.print("START");
    while (!digitalRead(bt_start));
  }
  else if(digitalRead(bt_stop) == 0) {
    digitalWrite(relay_1, LOW);
    status = 0;
    mLCD.clear();
    mLCD.setCursor(0, 0);
    mLCD.print("STOP");
    while (!digitalRead(bt_stop));
  }
  else if(digitalRead(bt_up) == 0) {
    val += 50;
    mLCD.clear();
    mLCD.setCursor(4, 1);
    mLCD.print(val);
    while (!digitalRead(bt_up));
  }
  else if(digitalRead(bt_down) == 0) {
    val -= 50;
    mLCD.setCursor(4, 1);
    mLCD.print(val);
    while (!digitalRead(bt_down));
  }
  Serial.println(String(digitalRead(sensor)) + "|" + String(status));
  if((digitalRead(sensor)) && (status)) {
    pumpRun(val);
  }
}

void initGPIO() {
  // Cấu hình các nút ấn làm ngõ vào và bật trở kéo lên
  pinMode(bt_start, INPUT_PULLUP);
  pinMode(bt_stop, INPUT_PULLUP);
  pinMode(bt_up, INPUT_PULLUP);
  pinMode(bt_down, INPUT_PULLUP);

  // Cấu hình sensor làm ngõ vào
  pinMode(sensor, INPUT);

  // Cấu hình các relay làm ngõ ra
  pinMode(relay_1, OUTPUT);
  pinMode(relay_2, OUTPUT);
}

void pumpRun(int ml) {
  delay(100);
  digitalWrite(relay_2, HIGH);
  delay(map(ml, 0, 1000, 0, 6000));
  digitalWrite(relay_2, LOW);
}