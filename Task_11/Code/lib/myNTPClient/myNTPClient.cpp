#include "myNTPClient.h"
#include <Arduino.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);

myNTPClient::myNTPClient() {
    ;
}

void myNTPClient::init() {
  timeClient.begin();
  timeClient.setTimeOffset(+7*60*60);
}

String myNTPClient::getTimeandDate() {
  while(!timeClient.update()) {
    timeClient.forceUpdate();
  }
  String formattedDate = timeClient.getFormattedDate();

  int splitT = formattedDate.indexOf("T");
  String dayStamp = formattedDate.substring(0, splitT);
  int temp1 = dayStamp.indexOf("-");
  String temp2 = dayStamp.substring(0, temp1);
  String temp3 = dayStamp.substring(temp1+1, dayStamp.length());
  int temp4 = temp3.indexOf("-");
  String temp5 = temp3.substring(0, temp4);
  String temp6 = temp3.substring(temp4+1, temp3.length());


  String timeStamp = formattedDate.substring(splitT+1, formattedDate.length()-1);

  return (timeStamp + " - " + temp6 + "/" + temp5 + "/" + temp2);
}