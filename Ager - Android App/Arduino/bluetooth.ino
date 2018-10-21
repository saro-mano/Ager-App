#include <DHT_U.h>
#include "DHT.h"
#define DHTPIN A5
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
char junk;
String inputString="";
int soil = A0;
int ldr = A2;
int output_value = 0;
int sensorValue = 0;

void setup()                    // run once, when the sketch starts
{
 Serial.begin(38400);            // set the baud rate to 9600, same should be of your Serial Monitor
 pinMode(8, OUTPUT);
 dht.begin();

}

void loop()
{  
  
   if(Serial.available()){
   while(Serial.available())
    {
      char inChar = (char)Serial.read(); //read the input
      inputString += inChar;        //make a string of the characters coming on serial
    }
     sensorValue = analogRead(ldr); // read the value from the sensor  
     float h = dht.readHumidity();
     float t = dht.readTemperature();
     output_value= analogRead(soil);
     output_value = map(output_value,550,0,0,100);
        Serial.print("Humidity: ");
        Serial.print(h);
        Serial.println("");
        Serial.print("Temperature: ");
        Serial.print(t);
        Serial.println("");
        Serial.print("Soil Humidity: ");
        Serial.print(output_value);
        Serial.println("");
        Serial.print("Light Intensity: ");
        Serial.print(sensorValue);
    while (Serial.available() > 0)  
    { junk = Serial.read() ; }      // clear the serial buffer
    if(inputString == "o"){
      while(1){
        output_value= analogRead(soil);
        output_value = map(output_value,550,0,0,100);
        if(output_value < 0){
        digitalWrite(8,HIGH);
       }
       else{
          digitalWrite(8,LOW);
        }
      }
    }
    if(inputString == "a"){         //in case of 'a' turn the LED on
      digitalWrite(8, HIGH);  
    }else if(inputString == "b"){   //incase of 'b' turn the LED off
      digitalWrite(8, LOW);
    }
    inputString = "";
  }
}


