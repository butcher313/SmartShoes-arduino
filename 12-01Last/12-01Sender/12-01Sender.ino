/*
지그비를 통해 수신부 아두이노에 데이터를 전송하는 아두이노 코드
*/

void setup() {
  // put your setup code here, to run once:
   Serial.begin(9600);
   while (Serial.available() && Serial.read()); // empty buffer again
}

void loop() {
  // put your main code here, to run repeatedly:

  // 압력센서들로부터 값을 읽어들인다. 
  int val0=analogRead(A0);
  int val1=analogRead(A1);
  int val2=analogRead(A2);

  int total = val0 + val1 + val2;
  
  // 데이터를 수신하는 아두이노로부터 신호가 오면 압력 데이터를 전송
  if(Serial.available()){
    byte data = Serial.read();
    if(data == 'A'){
      Serial.write(total/9);
    }
  }
}
