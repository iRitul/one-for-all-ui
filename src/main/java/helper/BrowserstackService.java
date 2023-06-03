package helper;

import utilities.PropertiesUtils;
import com.google.gson.JsonArray;
import exceptions.EnvironmentSetupException;
import org.apache.log4j.Logger;


public class BrowserstackService {
    PropertiesUtils propertiesUtils = new PropertiesUtils();
    private static final Logger LOG = Logger.getLogger(BrowserstackService.class);

    public String getBrowserstackAppId() {
        try {
            String[] curlCommandToFetchUploadedApk = new String[]{"curl -u \"" + propertiesUtils.getBrowserStackProperties("user") + ":" + propertiesUtils.getBrowserStackProperties("accesskey") + "\" -X GET \"https://api-cloud.browserstack.com/app-automate/recent_apps\""};
            CommandLineResponse recentAppUploadedToBrowserStackResponse = CommandLineExecutor.execCommand(curlCommandToFetchUploadedApk);
            JsonArray recentAppResponse = JsonFile.convertToArray(recentAppUploadedToBrowserStackResponse.getStdOut());
            return recentAppResponse.get(0)
                    .getAsJsonObject()
                    .get("app_url")
                    .getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EnvironmentSetupException("Unable to fetch browserstack app id" + e);
        }
    }

}
