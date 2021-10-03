
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>

// Variables de la red y firebase
#define WIFI_SSID "FAMILIA CARDONA"
#define WIFI_PASSWORD "24615540"
#define FIREBASE_HOST "esp8266-maria-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "KCRtZBF6xCtS7LGtYrMt42WA48ElaMw9g6n4Kgn8"


volatile int NumPulsos; //variable para la cantidad de pulsos recibidos
int PinSensor = 2;  //Sensor conectado en el pin D0 del esp8266
float factor_conversion = 7.11; //para convertir de frecuencia a caudal
float volumen = 0;
long dt = 0; //variación de tiempo por cada bucle
long t0 = 0; //millis() del bucle anterior



//---Función que se ejecuta en interrupción---------------
void ICACHE_RAM_ATTR ContarPulsos ()
{
  NumPulsos++;  //incrementamos la variable de pulsos
}

//---Función para obtener frecuencia de los pulsos--------
int ObtenerFrecuecia()
{
  int frecuencia;
  NumPulsos = 0;   //Ponemos a 0 el número de pulsos
  interrupts();    //Habilitamos las interrupciones
  delay(1000);   //muestra de 1 segundo
  noInterrupts(); //Deshabilitamos  las interrupciones
  frecuencia = NumPulsos; //Hz(pulsos por segundo)
  return frecuencia;
}
//---Funcion hora-----------
String getTime(){
 WiFiClient client;
    HTTPClient http;  
    String timeS = "";
    
    http.begin(client, "http://worldtimeapi.org/api/timezone/America/Bogota");
    int httpCode = http.GET();
    if (httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY) {
          String payload = http.getString();
          
          int beginS = payload.indexOf("unixtime");
          int endS = payload.indexOf("utc_datetime");
          Serial.println(payload);
          timeS = payload.substring(beginS + 10, endS - 2);    
    }
    return timeS;
}
void setup()
{


  pinMode(PinSensor, INPUT);
  attachInterrupt(digitalPinToInterrupt(PinSensor), ContarPulsos, RISING); //(Interrupción 0(Pin2),función,Flanco de subida)
  Serial.println ("Envie 'r' para restablecer el volumen a 0 Litros");
  t0 = millis();


  Serial.begin(9600);

  // conexion wifi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

}

int n = 0;
void loop ()
{

	delay(1000);
  if (Serial.available()) {
    if (Serial.read() == 'r')volumen = 0; //restablecemos el volumen si recibimos 'r'
  }    
  float frecuencia = ObtenerFrecuecia(); //obtenemos la frecuencia de los pulsos en Hz
  float caudal_L_m = frecuencia / factor_conversion; //calculamos el caudal en L/m
  dt = millis() - t0; //calculamos la variación de tiempo
  t0 = millis();
  volumen = volumen + (caudal_L_m / 60) * (dt / 1000); // volumen(L)=caudal(L/s)*tiempo(s)
   String Time = getTime();
 if (Time) {
   //float value;
   //value = Time.toFloat();     
   String path = "mediciones/"; 
   StaticJsonBuffer<200> jsonBuffer;
   JsonObject& data = jsonBuffer.createObject();
   data["time"] = Time;
   data["sensor"] = volumen;
   
  Firebase.push(path, data); 

  if (Firebase.failed()) {
    Serial.print("pushing /logs failed:");
    Serial.println(Firebase.error());
    return;
  }
 }

}
