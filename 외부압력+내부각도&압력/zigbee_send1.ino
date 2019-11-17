//압력센서 시작 송신쪽 아두이노 블루투스 필요없음
#include<SoftwareSerial.h>

//압력센서 끝

void setup()
{
  //압력센서 시작
  Serial.begin(9600);

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
  
  if(Serial.available()){
   
   byte data = Serial.read(); //보내달라는 신호 저장
   if(data == 'A'){
    Serial.write(val0);  
   }else if (data == 'B'){
    Serial.write(val1);
   }else if (data == 'C') {
    Serial.write(val2);
   }
  }
   /*
   if(
   
    Serial.println("압력 센서값 : " + String(val0));  
    Serial.println("압력 센서값 : " + String(val1));
    Serial.println("압력 센서값 : " + String(val2));
    
  }
   */
    //Serial.println("압력 센서값1 : " + String(val0)+ ", 압력 센서값2 : "+ String(val1)+ ",압력 센서값3 : "+String(val2));
    
    
    
    //압력센서 끝
    
}
