
int Motor_A_Enable = 9;
int Motor_A_Reverse = 4;
int Motor_A_Forward = 3;

int Motor_B_Enable = 10;
int Motor_B_Reverse = 7;
int Motor_B_Forward = 12;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  
  pinMode(Motor_A_Enable, OUTPUT);
  pinMode(Motor_A_Forward, OUTPUT);
  pinMode(Motor_A_Reverse, OUTPUT);

  pinMode(Motor_B_Enable, OUTPUT);
  pinMode(Motor_B_Forward, OUTPUT);
  pinMode(Motor_B_Reverse, OUTPUT);
}

void loop() {

  if(Serial.available() > 0)
  {
    char data;
    data = Serial.read();
    Serial.write(Serial.read());

    switch (data)
    {
      case '4': //FORWARD

        //delay(1000);
        
        digitalWrite(Motor_A_Reverse, LOW);
          digitalWrite(Motor_B_Reverse, LOW);
        
        digitalWrite(Motor_A_Forward, HIGH);
        digitalWrite(Motor_B_Forward, HIGH);



        for(int a = 0; a < 200; a++)
        {
          analogWrite(Motor_B_Enable, a);
          analogWrite(Motor_A_Enable, a);
          delay(5);
        }
        
        
        break;
      case '5': //REVERSE

        //delay(1000); 
        
        digitalWrite(Motor_A_Forward, LOW);
        digitalWrite(Motor_B_Forward, LOW);
      
        
        digitalWrite(Motor_A_Reverse, HIGH);
        digitalWrite(Motor_B_Reverse, HIGH);

        for(int a = 0; a < 200; a++)
        {
          analogWrite(Motor_B_Enable, a);
          analogWrite(Motor_A_Enable, a);
          delay(5);
        }
        
        
        break;
      case '7': //FORWARD LEFT

       /*
        for(int a = 0; a < 255; a++)
        {
          analogWrite(Motor_A_Enable, a);
          analogWrite(Motor_B_Enable, a);
          delay(5);
        }
        */

        analogWrite(Motor_A_Enable, 230);
          //analogWrite(Motor_B_Enable, 255);

          digitalWrite(Motor_A_Forward, HIGH);
        //  digitalWrite(Motor_B_Reverse, HIGH);
          digitalWrite(Motor_A_Reverse, LOW);
        //  digitalWrite(Motor_B_Forward, LOW);
        
        break;
      case '6': //FORWARD RIGHT
        
        /*
        for(int a = 0; a < 255; a++)
        {
          analogWrite(Motor_A_Enable, a);
          analogWrite(Motor_B_Enable, a);
          delay(5);
        } */
       // analogWrite(Motor_A_Enable, 250);
          analogWrite(Motor_B_Enable, 230);

          
       //  digitalWrite(Motor_A_Reverse, HIGH);
          digitalWrite(Motor_B_Forward, HIGH);
       //  digitalWrite(Motor_A_Forward, LOW);
          digitalWrite(Motor_B_Reverse, LOW);
        
        break;
      default: //If bluetooth module receives any value not listed above, both motors turn off
        analogWrite(Motor_A_Enable, 0);
        analogWrite(Motor_B_Enable, 0);
    }
  }
}
