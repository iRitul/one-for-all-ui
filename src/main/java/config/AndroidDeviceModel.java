package config;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AndroidDeviceModel {

    public AndroidDeviceModel getAndroidDeviceByName(String deviceName) {
        return Arrays.stream(androidDeviceModels).filter(androidDeviceModel -> androidDeviceModel.getName().equalsIgnoreCase(deviceName)).findFirst().get();
    }

    public AndroidDeviceModel getAndroidDeviceByName(String deviceName, int deviceIndex) {
        Stream<AndroidDeviceModel> androidDeviceModelStream = Arrays.stream(androidDeviceModels).filter(androidDeviceModel -> androidDeviceModel.getName().equalsIgnoreCase(deviceName));
        List<AndroidDeviceModel> androidDeviceModelList = androidDeviceModelStream.collect(Collectors.toList());
        return androidDeviceModelList.get(deviceIndex);
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
    private String appActivity;


    public String getName() {
        return name;
    }

    private AndroidDeviceModel[] androidDeviceModels;

    public AndroidDeviceModel(AndroidDeviceModel[] androidDeviceModels) {
        this.androidDeviceModels = androidDeviceModels;
    }

    public AndroidDeviceModel() {
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

    public String getAppActivity() {
        return appActivity;
    }
}
