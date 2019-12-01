
void setup() {
  // put your setup code here, to run once:
   Serial.begin(9600);
   while (Serial.available() && Serial.read()); // empty buffer again
}

void loop() {
  // put your main code here, to run repeatedly:

  int val0=analogRead(A0);
  int val1=analogRead(A1);
  int val2=analogRead(A2);

  int total = val0 + val1 + val2;

  if(Serial.available()){
    byte data = Serial.read();
    if(data == 'A'){
      Serial.write(total/9);
    }
  }
}
