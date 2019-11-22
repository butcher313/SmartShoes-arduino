# arduino

PPT :
https://docs.google.com/presentation/d/14mt9K2G-tIZFP0ZlM2DFFmUKFGopaxPIDIyHJa7xFqA/edit?ts=5dc13af1

블루투스 모듈 연결 방법 : 
https://blog.naver.com/PostView.nhn?blogId=eduino&logNo=221121406317

11-13 
 1. Only_Bluetooth.ino 코드를 사용, 아두이노에 블루투스만 연결하여 
핸드폰과 아두이노 사이의 양방향 메세지 전송 완료

 2. 압력센서를 연결하여 압력센서 값 안드로이드로 전송하기 완료
 
 3. 블루투스 통신 코드와 기존 안드로이드 앱 코드 합치기 
 
 4. 아두이노 UART 통신 :
 1) http://blog.naver.com/PostView.nhn?blogId=icbanq&logNo=221118276137&parentCategoryNo=27&categoryNo=&viewDate=&isShowPopularPosts=true&from=search
 2) http://www.iamamaker.kr/ko/tutorials/install_usb2uart_driver/
 3) https://www.youtube.com/watch?v=5AlOUPbSjZI
 
 5. Zigbee 세팅 
  1) http://blog.naver.com/PostView.nhn?blogId=roboholic84&logNo=220962532822&parentCategoryNo=&categoryNo=30&viewDate=&isShowPopularPosts=false&from=postView
  
  

<<2019 - 11 - 22>>

1. 데스크탑 환경으로 기울기값과 압력값 측정하는 아두이노 코드 실행하려하자 모종의 오류로 안됨

2. 기울기값이 포함된 코드는 실행이 안되므로 압력값을 가지고만 실험을 함

3. 어제 최종 업데이트된 Wedoino-GPS 어플리케이션과 아두이노 간의 블루투스 통신 원활하게 됨. 
   (회로상의 문제가 살짝 있었음) 아두이노에서 보내는 압력값을 안드로이드에서 잘 읽음

4. 지그비 통신에서 Serial.write()이 한 바이트씩만 전송하는 문제 때문에 Serial.wirte((char*)(&total), sizeof(int)) 를
   통해 전송을 시도함 수신하는 아두이노 코드는 Serial.read()로 하면 또 한바이트씩 받기 때문에 Serial.parseInt()를 
   사용 해봄. 그런데 pareInt()의 경우 입력을 받으면 그것을 long형으로 바꾸는 연산을 수행하기 때문에 내가 송신쪽 
아두이노의 압력센서를 누르는 동안에는 수신쪽 아두이노에서 압력값이 출력이 안됨. 그런데 누르는 것을 멈추면 출력이 됨.
즉 parseInt()가 들어오는 문자열을 연산하는 시간이 걸리기 때문 따라서 이 방법은 부적절

5. 4번의 개선을 위해 

while(Serial.available()){ //이거는 시리얼 모니터에 직접 타이핑할때만 전송이 가능한 경우

    buffer[bufferIndex] = Serial.read();
    bufferIndex++;
  }

  total_out = atoi(buffer);
  if(total_out != 0){

    BTSerial.println(String(total_in)); //블루투스를 통해 시리얼로 읽은것을 안드로이드로 전송
    BTSerial.println(String(total_out)); //블루투스를 통해 시리얼로 읽은것을 안드로이드로 전송
    Serial.println("압력 센서값 : " + String(total_in) + " 외부 압력센서 값 : " + String(total_out));
  }

  for(int i = 0; i < sizeof(buffer); i++){
    buffer[i] = NULL; 
  }
  bufferIndex = 0;

위와같이 한바이트씩 받아서 int형으로 바꾸는 방법도 시도해봄 그러나 int형으로 변환이 제대로 안됨 송신쪽 압력센서를 꾹 눌러도 
8 혹은 9 이런식으로 십진수 한자리숫자로만 변환이됨 따라서 이방법도 안됨

6. 마지막 방법으로는 그냥 수신부에서 한바이트씩 받고, 수신부의 코드상에서 외부압력값에 일정 값을 더하여 내부압력값과 수치를
맞춰주거나 내부 압력값을 0~255사이로 mapping하는 방법이다 이 방법이 가장 간단하면서도 확실하다. 다만 정확도에서는 
다소 문제가 있겠다. 
