#ifndef myHC_SR04_h
#define myHC_SR04_h

#include <Arduino.h>

class myHC_SR04
{
public:
  myHC_SR04();
  
  void init();
  int readDistance();

private:
  #define trig    19
  #define echo    18
};
#endif