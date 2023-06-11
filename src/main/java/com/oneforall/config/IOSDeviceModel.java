package com.oneforall.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOSDeviceModel {

    public IOSDeviceModel getIOSDeviceByName(String deviceName) {
        return Arrays.stream(iosDeviceModels).filter(iosDeviceModel -> iosDeviceModel.getName().equalsIgnoreCase(deviceName)).findFirst().get();
    }

    public IOSDeviceModel getIOSDeviceByName(String deviceName, int deviceIndex) {
        Stream<IOSDeviceModel> iosDeviceModelStream = Arrays.stream(iosDeviceModels).filter(androidDeviceModel -> androidDeviceModel.getName().equalsIgnoreCase(deviceName));
        List<IOSDeviceModel> iosDeviceModelList = iosDeviceModelStream.collect(Collectors.toList());
        return iosDeviceModelList.get(deviceIndex);
    }

    private String deviceName;
    private String platformName;
    private String platformVersion;
    private String automationName;
    private String app;
    private String appPackage;
    private String appWaitActivity;
    private String name;
    private String appPath;
    private Boolean fullReset;
    private int appWaitDuration;
    private String appiumVersion;
    private String udid;

    public String getName() {
        return name;
    }

    private IOSDeviceModel[] iosDeviceModels;

    public IOSDeviceModel(IOSDeviceModel[] iosDeviceModels) {
        this.iosDeviceModels = iosDeviceModels;
    }

    public IOSDeviceModel() {
    }


    public String getDeviceName() {
        return deviceName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getAutomationName() {
        return automationName;
    }

    public String getApp() {
        return app;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppWaitActivity() {
        return appWaitActivity;
    }

    public String getAppPath() {
        return appPath;
    }

    public Boolean getFullReset() {
        return fullReset;
    }

    public int getAppWaitDuration() {
        return appWaitDuration;
    }

    public String getAppiumVersion() {
        return appiumVersion;
    }

    public String getUDID() {
        return udid;
    }
}
