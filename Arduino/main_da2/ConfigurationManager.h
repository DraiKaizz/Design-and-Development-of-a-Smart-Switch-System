#ifndef CONFIGURATIONMANAGER_H
#define CONFIGURATIONMANAGER_H

#include <Arduino.h>
#include <LittleFS.h>  // Hoặc #include <EEPROM.h> nếu bạn sử dụng EEPROM

class ConfigurationManager {
public:
    ConfigurationManager();
    bool begin();  // Khởi tạo FS hoặc EEPROM
    bool saveConfig(const String& wifiSSID, const String& wifiPassword, const String& firebaseEmail, const String& firebasePassword);
    bool loadConfig(String& wifiSSID, String& wifiPassword, String& firebaseEmail, String& firebasePassword);
    bool clearConfig();  // Xóa cấu hình
private:
    const String configFilePath = "/config.txt";  // Đường dẫn file lưu cấu hình
};

#endif
