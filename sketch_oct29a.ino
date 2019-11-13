int LEDout = 3;

void setup() {
  Serial.begin(9600);
}

void loop() {
  int val = analogRead(A0);
  int val1 = analogRead(A1);
  int val2 = analogRead(A2);
 
  int mappedValue = map(val, 0, 1023, 0, 255);
  int mappedValue1 = map(val1, 0, 1023, 0, 255);
  /*map(value, fromLow, fromHigh, toLow, toHigh);
   * 
   * value : 매핑하고자 하는 값
   * fromLow : 현재 범위의 하한 값
   * fromHigh : 현재 범위의 상한 값
   * toLow : 매핑하고자 하는 범위의 하한 값
   * toHigh : 매핑하고자 하는 범위의 상한 값
   */
  analogWrite(LEDout, mappedValue);

  Serial.println("Analog Input 1: " + String(val) + ", 2: " + String(val1) + ", 3: " + String(val2));

  delay(1000);
}
