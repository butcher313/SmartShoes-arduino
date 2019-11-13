//압력센서 시작
#include<SoftwareSerial.h>
SoftwareSerial BTSerial(7,8);
//압력센서 끝

void setup()
{
  //압력센서 시작
  Serial.begin(9600);
  BTSerial.begin(9600);
  //압력센서 끝
}

void loop()
{
  //압력 센서 시작
  int val0=analogRead(A0);
  int val1=analogRead(A1);
  int val2=analogRead(A2);
  int mappedValue0=map(val0,0,1023,0,255);
  int mappedValue1=map(val1,0,1023,0,255);
  int mappedValue2=map(val2,0,1023,0,255);
  
  if(BTSerial.available()){
    Serial.write(BTSerial.read());
  }
  if(Serial.available()){
    
    BTSerial.write(Serial.read());
    BTSerial.println("압력 센서 값: " + String(val0));
    Serial.println("압력 센서값 : " + String(val0));

    BTSerial.println("압력 센서 값: " + String(val1));
    Serial.println("압력 센서값 : " + String(val1));

    BTSerial.println("압력 센서 값: " + String(val2));
    Serial.println("압력 센서값 : " + String(val2));
    
    
    
  }
    BTSerial.println("압력 센서 값: " + String(val0)+String(val1)+String(val2));
    Serial.println("압력 센서값1 : " + String(val0)+ ", 압력 센서값2 : "+ String(val1)+ ",압력 센서값3 : "+String(val2));
    //압력센서 끝

//진동센서 시작
analogWrite(3, 0);
delay(1000);
analogWrite(3, 0);
delay(1000);
//진동 센서 끝
}
