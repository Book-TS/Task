#include <FastLED.h>

#define LED_PIN     6
#define NUM_LEDS    16

CRGB leds[NUM_LEDS];

void printLED(int r, int g, int b);
void shiftLeft(int r, int g, int b);
void shiftRight(int r, int g, int b);

void setup() {
  FastLED.addLeds<WS2812, LED_PIN, GRB>(leds, NUM_LEDS);
  // FastLED.setBrightness(100);                   // Đặt độ sáng của LED
}

void loop() {
  shiftRight(255, 0, 0);         // Dịch LED đỏ trái sang phải 
  delay(200);
  shiftLeft(255, 0, 0);          // Dịch LED đỏ phải sang trái
  delay(1000);
  
  shiftRight(0, 255, 0);         // Dịch LED xanh lá trái sang phải
  delay(200);
  shiftLeft(0, 255, 0);          // Dịch LED xanh lá phải sang trái
  delay(1000);
  
  shiftRight(0, 0, 255);         // Dịch LED xanh dương trái sang phải
  delay(200);
  shiftLeft(0, 0, 255);          // Dịch LED xanh dương phải sang trái
  delay(1000);

  printLED(255, 0, 0);           // Sáng LED đỏ
  delay(1000);
  printLED(0, 255, 0);           // Sáng LED xanh lá
  delay(1000);
  printLED(0, 0, 255);           // Sáng LED xanh dương
  delay(1000);
  printLED(0, 0, 0);             // Tắt toàn bộ LED
}

// Hiển thị màu
void printLED(int r, int g, int b) {
  fill_solid(leds, NUM_LEDS, CRGB(r, g, b));    // Thiết lập màu cho tất cả các LED
  FastLED.show();                               // Hiển thị màu sắc lên LED Ring
}

// Dịch trái sang phải
void shiftRight(int r, int g, int b) {
  for(int i = 0; i < NUM_LEDS; i++) {
    leds[i] = CRGB(r, g, b);                    // Thiết lập màu cho 1 LED
    FastLED.show();                             // Hiển thị màu sắc lên LED Ring
    delay(50);
    leds[i] = CRGB(0, 0, 0);
  }
}

// Dịch phải sang trái
void shiftLeft(int r, int g, int b) {
  for(int i = NUM_LEDS; i >= 0; i--) {
    leds[i] = CRGB(r, g, b);                    // Thiết lập màu cho 1 LED
    FastLED.show();                             // Hiển thị màu sắc lên LED Ring
    delay(50);
    leds[i] = CRGB(0, 0, 0);
  }
}