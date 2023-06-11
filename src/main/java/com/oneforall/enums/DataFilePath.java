package com.oneforall.enums;

public enum DataFilePath {
    BROWSERSTACK_FILE_PATH(System.getProperty("user.dir") + "/src/main/resources/browserstack.properties"),
    ANDROID_DEVICE_PATH("src/main/resources/androidDevices.json"),
    IOS_DEVICE_PATH("src/main/resources/iOSDevices.json"),
    TEST_DATA_PATH("src/main/resources/test_data.json");
    private String value;
    DataFilePath(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
