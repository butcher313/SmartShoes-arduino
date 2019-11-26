#include<SoftwareSerial.h>
SoftwareSerial BTSerial(7, 8);

unsigned long time_prev, time_cur;
int ext_press = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  BTSerial.begin(9600);

  while (Serial.available() && Serial.read()); // empty buffer again
  time_prev = millis();
}

void loop() {
  // put your main code here, to run repeatedly:
  time_cur = millis();

  int val0=analogRead(A0);
  int val1=analogRead(A1);
  int val2=analogRead(A2);

  int total_in = val0 + val1 + val2;

           if(time_cur - time_prev > 500) {
              time_prev = time_cur;

              if(Serial.available()){
                 ext_press = Serial.read() * 5;
             
                 Serial.println( "오른발 압력 값 : " + String(total_in) + " 왼발 압력 값 : " + String(ext_press));
  
                 BTSerial.println(String(total_in) + 'A' + String(0) + 'A'+ String(ext_press)+ 'A' + String(0));          
            }      
    }
}
