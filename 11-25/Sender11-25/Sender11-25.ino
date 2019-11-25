unsigned long time_prev, time_cur; //시간 측정 위한 변수 

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  while (Serial.available() && Serial.read()); // empty buffer again

  time_prev = millis();
}

void loop() {
  // put your main code here, to run repeatedly:
  time_cur = millis();
  
  int val0=analogRead(A0);
  int val1=analogRead(A1);
  int val2=analogRead(A2);

  int total_press = val0 + val1 + val2;
  total_press = total_press / 5;
  
    if(time_cur - time_prev > 1000){
              time_prev = time_cur;
              
              Serial.write(total_press);
             

              //Serial.print("압력 합친 것 : " + String(total_press) + "\n");
              //Serial.print("기울기 값 : " + String(ypr[0] * 180 / M_PI) + "\n");
            }

}
