#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h> // Thư viện HTTPClient cho ESP8266
#include <ESP8266WebServer.h>
#include <EEPROM.h>
#include <Firebase_ESP_Client.h>
#include <addons/TokenHelper.h>
#include <addons/RTDBHelper.h>
#include <WiFiClient.h>

#define API_KEY "AIzaSyDWW44FIwhro0g0GeBmXvvYQAXXczPwPxU"
#define DATABASE_URL "https://fir-48ded-default-rtdb.firebaseio.com/"

ESP8266WebServer    server(80);

// Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
unsigned long count = 0;

const int LED1 = D1; // Chân D5
const int LED2 = D2; // Chân D6
const int buttonPin1 = D5; // Chân D5
const int buttonPin2 = D6; // Chân D6
const int led_wf = D8; // Chân D8
const unsigned long buttonHoldTime = 6000; // Thời gian giữ nút nhấn trong 6 giây (6000ms)
unsigned long button1PressTime = 0;
unsigned long button2PressTime = 0;

// bien nut nhan
int button1State = 0; // Biến lưu trạng thái của nút nhấn
int lastButton1State = 0; // Biến lưu trạng thái nút nhấn trước đó
bool led1State = false; // Biến lưu trạng thái của LED
int button2State = 0; // Biến lưu trạng thái của nút nhấn
int lastButton2State = 0; // Biến lưu trạng thái nút nhấn trước đó
bool led2State = false; // Biến lưu trạng thái của LED

int save1 = 0;
int save2 = 0;

struct settings {
  char ssid[30];
  char password[30];
  char user_email[30];
  char firebase_password[30];
} user_device = {};

void setup() {
  Serial.begin(115200);
  pinMode(buttonPin1, INPUT_PULLUP); // Thiết lập chân GPIO của nút nhấn là đầu vào với pull-up nội bộ
  pinMode(buttonPin2, INPUT_PULLUP); // Thiết lập chân GPIO của nút nhấn là đầu vào với pull-up nội bộ
  pinMode(led_wf, OUTPUT); // Thiết lập chân GPIO của đèn LED là đầu ra
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);

  EEPROM.begin(sizeof(struct settings) );
  EEPROM.get( 0, user_device);
   
  WiFi.mode(WIFI_STA);
  WiFi.begin(user_device.ssid, user_device.password);
  
  byte tries = 0;
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print("Attempting to connect... Status: ");
    Serial.println(WiFi.status());
    
    if (tries++ > 10) {
      // Không thể kết nối, chuyển sang chế độ Access Point
      WiFi.mode(WIFI_AP);
      WiFi.softAP("Setup Portal", "123451234");
      Serial.println("Failed to connect. Starting AP mode.");
      for (int i = 0; i < 5; i++) {
        digitalWrite(led_wf, HIGH);
        delay(1000); // Đèn LED sáng
        digitalWrite(led_wf, LOW);
        delay(1000); // Đèn LED tắt
      }
      break;
    }
  }

  // Kiểm tra nếu kết nối thành công
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("Connected to WiFi successfully!");
    Serial.print("IP Address: ");
    Serial.println(WiFi.localIP());
    /* Assign the api key (required) */
    config.api_key = API_KEY;

    /* Assign the user sign in credentials */
    auth.user.email = user_device.user_email;
    auth.user.password = user_device.firebase_password;

    /* Assign the RTDB URL (required) */
    config.database_url = DATABASE_URL;
    Firebase.begin(&config, &auth);
    // Comment or pass false value when WiFi reconnection will control by your code or third party library e.g. WiFiManager
    Firebase.reconnectNetwork(true);
  }

  server.on("/",  handlePortal);
  server.begin();


}


void loop() {

  server.handleClient();

  bool button1State = digitalRead(buttonPin1) == LOW;
  bool button2State = digitalRead(buttonPin2) == LOW;
  if (Firebase.ready() && (millis() - sendDataPrevMillis > 600 || sendDataPrevMillis == 0))
  {
    sendDataPrevMillis = millis();
    // Xử lý nút nhấn để reset cấu hình WiFi
    if (!button1State && !button2State) { // Nếu cả hai nút nhấn đều được nhấn
      if (button1PressTime == 0 && button2PressTime == 0) {
        button1PressTime = millis(); // Lưu thời gian khi cả hai nút nhấn được nhấn
        button2PressTime = millis();
      } else if (millis() - button1PressTime >= buttonHoldTime && millis() - button2PressTime >= buttonHoldTime) {
        Serial.println("Both buttons pressed, resetting WiFi settings...");
        ESP.restart();
        delay(700); // Chờ 1 giây để đảm bảo lệnh reset được thực hiện
      }
    } else {
      button1PressTime = 0; // Reset thời gian nếu một trong hai nút nhấn được thả ra
      button2PressTime = 0;
    }
      // Kiểm tra kết nối Internet
    if (checkInternetConnectionWithHTTP()) {
      digitalWrite(led_wf, HIGH);
      writeData();
      readData();
    } else {
      digitalWrite(led_wf, LOW);
      controll_device();
    }

    // Kiểm tra dữ liệu từ Firebase (nếu có)
  /* if (Firebase.RTDB.getInt(&fbdo, "/Devices/Device State 1")) {
      int value = fbdo.intData();  // Lấy dữ liệu dưới dạng số nguyên
      Serial.printf("Get integer... %d\n", value);
    } else {
      Serial.printf("Failed to get integer... %s\n", fbdo.errorReason().c_str());
    }*/

  }
}


void controll_device(){
    // Xử lý nút bấm 1
  button1State = digitalRead(buttonPin1); // Đọc trạng thái nút nhấn
  // Kiểm tra sự thay đổi trạng thái nút nhấn
  if (button1State != lastButton1State) {
    if (button1State == HIGH) {
      // Nếu trạng thái nút nhấn thay đổi thành HIGH
      led1State = !led1State; // Đảo trạng thái của LED
      digitalWrite(LED1, led1State ? HIGH : LOW); // Cập nhật trạng thái LED
      Serial.println(led1State ? "LED ON" : "LED OFF"); // In ra trạng thái LED
      delay(200); // Đợi 200ms để tránh nhấn liên tục
    }
  }
  lastButton1State = button1State; // Cập nhật trạng thái nút nhấn trước đó

    // Xử lý nút bấm 2
  button2State = digitalRead(buttonPin2); // Đọc trạng thái nút nhấn
  // Kiểm tra sự thay đổi trạng thái nút nhấn
  if (button2State != lastButton2State) {
    if (button2State == HIGH) {
      // Nếu trạng thái nút nhấn thay đổi thành HIGH
      led2State = !led2State; // Đảo trạng thái của LED
      digitalWrite(LED2, led2State ? HIGH : LOW); // Cập nhật trạng thái LED
      Serial.println(led2State ? "LED ON" : "LED OFF"); // In ra trạng thái LED
      delay(200); // Đợi 200ms để tránh nhấn liên tục
    }
  }
  lastButton2State = button2State; // Cập nhật trạng thái nút nhấn trước đó
}

void readData(){

  if (Firebase.RTDB.getInt(&fbdo, "/Devices/Device State 1")) {
      save1 = fbdo.intData();  // Lấy dữ liệu dưới dạng số nguyên
      Serial.print("Device State 1: ");
      Serial.println(save1);
  } else {
      Serial.printf("Failed to get integer... %s\n", fbdo.errorReason().c_str());
  }
  if (Firebase.RTDB.getInt(&fbdo, "/Devices/Device State 2")) {
      save2 = fbdo.intData();  // Lấy dữ liệu dưới dạng số nguyên
      Serial.print("Device State 2: ");
      Serial.println(save2);
  } else {
      Serial.printf("Failed to get integer... %s\n", fbdo.errorReason().c_str());
  }

  // Cập nhật trạng thái LED
  led1State = save1;
  led2State = save2;
  if (save1 == 1) {
    digitalWrite(LED1, HIGH);
  } else{
    digitalWrite(LED1, LOW);    
  }

  if (save2 == 1) {
    digitalWrite(LED2, HIGH);
  } else{
    digitalWrite(LED2, LOW);    
  }
}


void writeData(){
  // Xử lý nút bấm 1
  button1State = digitalRead(buttonPin1); // Đọc trạng thái nút nhấn
  // Kiểm tra sự thay đổi trạng thái nút nhấn
  if (button1State != lastButton1State) {
    if (button1State == HIGH) {
      // Nếu trạng thái nút nhấn thay đổi thành HIGH
      led1State = !led1State; // Đảo trạng thái của LED
      Firebase.RTDB.setInt(&fbdo, "Devices/Device State 1", led1State ? 1 : 0);
      Serial.println(led1State ? "LED ON" : "LED OFF"); // In ra trạng thái LED
      delay(200); // Đợi 200ms để tránh nhấn liên tục
    }
  }
  lastButton1State = button1State; // Cập nhật trạng thái nút nhấn trước đó

    // Xử lý nút bấm 2
  button2State = digitalRead(buttonPin2); // Đọc trạng thái nút nhấn
  // Kiểm tra sự thay đổi trạng thái nút nhấn
  if (button2State != lastButton2State) {
    if (button2State == HIGH) {
      // Nếu trạng thái nút nhấn thay đổi thành HIGH
      led2State = !led2State; // Đảo trạng thái của LED
      Firebase.RTDB.setInt(&fbdo, "Devices/Device State 2", led2State ? 1 : 0);
      Serial.println(led2State ? "LED ON" : "LED OFF"); // In ra trạng thái LED
      delay(200); // Đợi 200ms để tránh nhấn liên tục
    }
  }
  lastButton2State = button2State; // Cập nhật trạng thái nút nhấn trước đó
}


bool checkInternetConnectionWithHTTP() {
  WiFiClient client;
  const char* host = "www.google.com";
  const uint16_t port = 80; // HTTP

  client.setTimeout(500); // Thay đổi thời gian chờ nếu cần
  if (client.connect(host, port)) {
    return true;
  } else {
    return false;
  }
}


void handlePortal() {

  if (server.method() == HTTP_POST) {

    strncpy(user_device.ssid,     server.arg("ssid").c_str(),     sizeof(user_device.ssid) );
    strncpy(user_device.password, server.arg("password").c_str(), sizeof(user_device.password) );

    strncpy(user_device.user_email,     server.arg("user_email").c_str(),     sizeof(user_device.user_email) );
    strncpy(user_device.firebase_password, server.arg("firebase_password").c_str(), sizeof(user_device.firebase_password) );

    user_device.ssid[server.arg("ssid").length()] = user_device.password[server.arg("password").length()] = '\0';
    user_device.user_email[server.arg("user_email").length()] = user_device.firebase_password[server.arg("firebase_password").length()] = '\0';

    EEPROM.put(0, user_device);
    EEPROM.commit();

    // Hiển thị thông báo thành công
    server.send(200, "text/html", 
        "<!doctype html><html lang='en'><head>"
        "<meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'>"
        "<title>Setup Complete</title>"
        "<style>body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }"
        ".container { max-width: 800px; margin: auto; background: #fff; padding: 20px; border-radius: 5px; }"
        "h1 { text-align: center; }"
        "p { text-align: center; }</style>"
        "</head><body><div class='container'><h1>Setup Complete</h1>"
        "<p>Your settings have been saved successfully! Please restart the device.</p></div></body></html>");
  } else {
    // Hiển thị trang cấu hình
    server.send(200, "text/html", 
        "<!doctype html><html lang='en'><head>"
        "<meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'>"
        "<title>ESP Configuration</title>"
        "<style>body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }"
        ".container { max-width: 800px; margin: auto; background: #fff; padding: 20px; border-radius: 5px; }"
        "h1 { text-align: center; }"
        "label { display: block; margin: 10px 0 5px; }"
        "input[type='text'], input[type='password'] { width: calc(100% - 22px); padding: 10px; margin: 5px 0 20px; border: 1px solid #ccc; border-radius: 5px; }"
        "input[type='submit'] { background-color: #28a745; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; }"
        "input[type='submit']:hover { background-color: #218838; }"
        "h3 { margin-top: 20px; }</style>"
        "</head><body><div class='container'><form action='/' method='post'>"
        "<h1>ESP Configuration</h1>"
        "<h3>WiFi Configuration</h3>"
        "<label for='ssid'>WiFi SSID:</label>"
        "<input type='text' id='ssid' name='ssid' placeholder='Enter WiFi SSID'><br>"
        "<label for='password'>WiFi Password:</label>"
        "<input type='password' id='password' name='password' placeholder='Enter WiFi Password'><br>"
        "<h3>Firebase Configuration</h3>"
        "<label for='email'>Firebase Email:</label>"
        "<input type='text' id='user_email' name='user_email' placeholder='Enter Firebase Email'><br>"
        "<label for='firebase_password'>Firebase Password:</label>"
        "<input type='password' id='firebase_password' name='firebase_password' placeholder='Enter Firebase Password'><br>"
        "<input type='submit' value='Save'></form></div></body></html>");
  }
}