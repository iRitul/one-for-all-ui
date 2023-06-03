package helper;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;


public class SecretManager {

    public static String getHostName() {
        String secret = getSecret();
        return (secret.split(",")[3]).split(":")[1].replaceAll("\"", "");
    }

    public static String getPortNumber() {
        String secret = getSecret();
        return (secret.split(",")[4]).split(":")[1].replaceAll("\"", "");
    }

    public static String getDatabaseName() {
        String secret = getSecret();
        return (secret.split(",")[5]).split(":")[1].replaceAll("\"", "");
    }

    public static String getUserName() {
        String secret = getSecret();
        return (secret.split(",")[0]).split(":")[1].replaceAll("\"", "");
    }

    public static String getPassword() {
        String secret = getSecret();
        return (secret.split(",")[1]).split(":")[1].replaceAll("\"", "");
    }

    private static String getSecret() {

        String secretName = "secret_name";
        Region region = Region.of("region");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            // For a list of exceptions thrown, see
            // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
            throw e;
        }

        String secret = getSecretValueResponse.secretString();
        return secret;
    }
}
