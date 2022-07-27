package notifir;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MockServer {

  public static final String CREATE_NOTIFICATION_201 = "src/test/resources/create_notification_201.json";
  public static final String CREATE_NOTIFICATION_401 = "src/test/resources/create_notification_401.json";
  public static final String CREATE_NOTIFICATION_401_HMAC = "src/test/resources/create_notification_401_hmac.json";
  public static final String CREATE_NOTIFICATION_403 = "src/test/resources/create_notification_403.json";

  private final MockWebServer server;

  public MockServer() throws Exception {
    server = new MockWebServer();
    server.start();
  }

  public void stop() throws IOException {
    server.shutdown();
  }

  public String getBaseUrl() {
    return server.url("/").toString();
  }

  public RecordedRequest takeRequest() throws InterruptedException {
    return server.takeRequest();
  }

  private String readTextFile(String path) throws IOException {
    return new String(Files.readAllBytes(Paths.get(path)));
  }

  public void jsonResponse(String path, int statusCode) throws IOException {
    MockResponse response = new MockResponse()
        .setResponseCode(statusCode)
        .addHeader("Content-Type", "application/json")
        .setBody(readTextFile(path));
    server.enqueue(response);
  }

  public void noContentResponse() {
    MockResponse response = new MockResponse()
        .setResponseCode(204)
        .addHeader("Content-Type", "application/json")
        .setBody("");
    server.enqueue(response);
  }

}
