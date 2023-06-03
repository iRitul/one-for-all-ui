package utilities;

import enums.DataFilePath;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.util.Properties;

public class PropertiesUtils {
    private static final Logger LOG = Logger.getLogger(PropertiesUtils.class);
    private static final Properties browserStackProperties;

    static {
        browserStackProperties = loadProperty(DataFilePath.BROWSERSTACK_FILE_PATH.getValue());
    }

    public String getBrowserStackProperties(String key) {
        return browserStackProperties.getProperty(key);
    }

    public static Properties loadProperty(String filePath) {
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(new FileReader(filePath));
        } catch (Exception e) {
            properties.clear();
            LOG.error(filePath + " not found");
        }
        return properties;
    }
}
