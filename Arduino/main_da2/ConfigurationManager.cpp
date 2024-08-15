#include "ConfigurationManager.h"

ConfigurationManager::ConfigurationManager() {
    // Constructor
}

bool ConfigurationManager::begin() {
    if (!LittleFS.begin()) {
        Serial.println("Failed to mount file system");
        return false;
    }
    return true;
}

bool ConfigurationManager::saveConfig(const String& wifiSSID, const String& wifiPassword, const String& firebaseEmail, const String& firebasePassword) {
    File configFile = LittleFS.open(configFilePath, "w");
    if (!configFile) {
        Serial.println("Failed to open config file for writing");
        return false;
    }

    configFile.println(wifiSSID);
    configFile.println(wifiPassword);
    configFile.println(firebaseEmail);
    configFile.println(firebasePassword);
    configFile.close();
    return true;
}

bool ConfigurationManager::loadConfig(String& wifiSSID, String& wifiPassword, String& firebaseEmail, String& firebasePassword) {
    File configFile = LittleFS.open(configFilePath, "r");
    if (!configFile) {
        Serial.println("Failed to open config file for reading");
        return false;
    }

    wifiSSID = configFile.readStringUntil('\n');
    wifiPassword = configFile.readStringUntil('\n');
    firebaseEmail = configFile.readStringUntil('\n');
    firebasePassword = configFile.readStringUntil('\n');
    configFile.close();
    return true;
}

bool ConfigurationManager::clearConfig() {
    if (LittleFS.remove(configFilePath)) {
        return true;
    } else {
        Serial.println("Failed to remove config file");
        return false;
    }
}
