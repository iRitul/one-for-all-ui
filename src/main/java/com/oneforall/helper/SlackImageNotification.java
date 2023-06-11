package com.oneforall.helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.oneforall.helper.ImageFinder.findPngFilePaths;

public class SlackImageNotification {
    private static final Logger LOGGER = Logger.getLogger(SlackImageNotification.class);
    private static String url = "https://slack.com/api/files.upload";
    private static String token = "channelToken";
    private static String channel_alert = "channelId";

    public static void getScreenShots() {
        String folderPath = "Screenshots";
        List<String> pngFilePaths = findPngFilePaths(folderPath);

        if (!pngFilePaths.isEmpty()) {
            System.out.println("PNG files found:");
            int count = 1;
            for (String pngFilePath : pngFilePaths) {
                LOGGER.info(pngFilePath);
                SlackImageNotification.postScreenShots(pngFilePath, "Failure Screenshot " + count,channel_alert);
                count++;
            }
        } else {
            LOGGER.info("No PNG files found in the specified folder.");
        }
    }

    public static void postScreenShots(String filePath, String initialComment,String channel) {
        File file = new File(filePath);
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("multipart/form-data")
                .multiPart("file", file, "image/png")
                .multiPart("initial_comment", initialComment)
                .multiPart("channels", channel)
                .post(url);

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();

        System.out.println("Status code: " + statusCode);
        System.out.println("Response body: " + responseBody);
    }

    public static void deleteScreenFolder(String folderName) {
        try {
            FileUtils.deleteDirectory(new File(folderName));
            LOGGER.info(folderName + " Folder deleted successfully. Ready to store fresh screenshots ..!");
        } catch (IOException e) {
            LOGGER.error("Failed to delete the folder: " + e.getMessage());
        }
    }

}








