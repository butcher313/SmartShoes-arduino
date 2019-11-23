void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
 
}


void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available()){
    char receive = Serial.read();
   //Serial.println(receive);

    if(receive == 'A'){
      Serial.write('B');
      //Serial.println("receive A");
    }
    
  }
   
    
            
}
