#ifndef myNTPClient_h
#define myNTPClient_h

#include <Arduino.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

class myNTPClient
{
public:
  myNTPClient();
  
  void init();
  String getTimeandDate();

private:
};
#endif