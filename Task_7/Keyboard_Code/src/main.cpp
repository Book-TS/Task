#include <Arduino.h>

const int pinOut[] = {14, 15, 16, 17, 18};
const int pinIn[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 19, 20, 21};
const String KeyCap[5][15] = {
    {"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="},
    {"Tab", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]"},
    {"CAPS", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "ENTER"},
    {"SHIFT", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "UP", "SHIFT", "/"},
    {"CTRL", "GUI", "ALT", "NULL", "NULL", "NULL", "SPACE", "LEFT", "DOWN", "RIGHT", "CTRL", "NO", "BACK"}};

void scanKey();

void setup()
{
  Serial.begin(9600);
  for (size_t i = 0; i < 5; i++)
    pinMode(pinOut[i], OUTPUT);
  for (size_t i = 0; i < 13; i++)
    pinMode(pinIn[i], INPUT_PULLUP);

  for (size_t i = 0; i < 5; i++)
    digitalWrite(pinOut[i], HIGH);

  pinMode(A7, OUTPUT);
  digitalWrite(A7, 1);
}

void loop()
{
  scanKey();
}

void scanKey()
{
  for (size_t i = 0; i < 5; i++)
  {
    digitalWrite(pinOut[i], LOW);
    for (size_t j = 0; j < 13; j++)
    {
      if (digitalRead(pinIn[j]) == 0)
      {
        Serial.println(KeyCap[i][j]);
        while (!digitalRead(pinIn[j]))
          ;
      }
    }
    digitalWrite(pinOut[i], HIGH);
  }
}