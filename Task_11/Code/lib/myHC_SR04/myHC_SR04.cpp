#include "myHC_SR04.h"
#include <Arduino.h>

myHC_SR04::myHC_SR04() {
    ;
}

void myHC_SR04::init() {
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);
}

int myHC_SR04::readDistance() {
  digitalWrite(trig,0);
  delayMicroseconds(2);
  digitalWrite(trig,1);
  delayMicroseconds(10); 
  digitalWrite(trig,0);
  return int(pulseIn(echo,HIGH)/2/29.412);
}