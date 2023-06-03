package helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.SlackMessage;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.util.Formatter;

public class SlackUtils {
    private static String slackWebhookUrl = "https://hooks.slack.com/services/"+"webHookId";

    public static void sendMessage(SlackMessage message) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(slackWebhookUrl);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(message);

            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            client.execute(httpPost);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateSlackReport(int passed, int Failed, int Skipped, int percentage) {
        Formatter fmt = new Formatter();
        fmt.format("%15s%15s%15s%15s\n", "Passed", "Failed", "Skipped", "Percentage (%)");
        fmt.format("%15s%15s%15s%15s\n", passed, Failed, Skipped, percentage + "%");
        SlackMessage slackMessage = SlackMessage.builder()
                .username("AutomationReport")
                .text(String.valueOf(fmt))
                .build();
        SlackUtils.sendMessage(slackMessage);
    }


}

