package config;

import utilities.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.DataFilePath;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;

public class DeviceConfig {
    private static final Logger LOG = Logger.getLogger(DeviceConfig.class);

    public AndroidDeviceModel readAndroidDeviceConfig() throws IOException {
        byte[] jsonData = null;
        ObjectMapper objectMapper = new ObjectMapper();
        jsonData = Files.readAllBytes(org.apache.commons.io.FileUtils.getFile(FileUtils.getFile(DataFilePath.ANDROID_DEVICE_PATH.getValue())).toPath());
        AndroidDeviceModel[] androidDeviceModels = objectMapper.readValue(jsonData, AndroidDeviceModel[].class);
        return new AndroidDeviceModel(androidDeviceModels);
    }

    public IOSDeviceModel readIOSDeviceConfig() throws IOException {
        byte[] jsonData = null;
        ObjectMapper objectMapper = new ObjectMapper();
        jsonData = Files.readAllBytes(org.apache.commons.io.FileUtils.getFile(FileUtils.getFile(DataFilePath.IOS_DEVICE_PATH.getValue())).toPath());
        IOSDeviceModel[] iosDeviceModels = objectMapper.readValue(jsonData, IOSDeviceModel[].class);
        return new IOSDeviceModel(iosDeviceModels);
    }

    public UserDetailsModel readTestData() throws IOException {
        byte[] jsonData = null;
        ObjectMapper objectMapper = new ObjectMapper();
        String file = DataFilePath.TEST_DATA_PATH.getValue();
        LOG.info("file Name: " + file);
        jsonData = Files.readAllBytes(org.apache.commons.io.FileUtils.getFile(FileUtils.getFile(file)).toPath());
        UserDetailsModel[] userDetailModels = objectMapper.readValue(jsonData, UserDetailsModel[].class);
        return new UserDetailsModel(userDetailModels);
    }

}
