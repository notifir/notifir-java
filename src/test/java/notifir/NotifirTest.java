package notifir;

import static notifir.Matchers.hasHeader;
import static notifir.Matchers.hasMethodAndPath;
import static notifir.MockServer.CREATE_NOTIFICATION_201;
import static notifir.MockServer.CREATE_NOTIFICATION_401;
import static notifir.MockServer.CREATE_NOTIFICATION_401_HMAC;
import static notifir.MockServer.CREATE_NOTIFICATION_403;
import static notifir.Notifir.SYSTEM_USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import notifir.exception.NotifirApiException;
import notifir.http.Request;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Set;


public class NotifirTest {

  public static final boolean mock = true;
  public static final String BASE_URL = "http://localhost:3001/api";
  public static final String API_PUBLIC_KEY = "114ee1da-067b-11ed-be0f-6f24634ae754";
  public static final String API_SECRET_KEY = "114ee22a-067b-11ed-be0f-079d8f6dbc6b";

  private MockServer server;
  private Notifir api;
  private String baseUrl;

  NotificationRequest testNotification = NotificationRequest
      .builder()
      .type("test")
      .recipients(Set.of("user@test.com"))
      .payload(new HashMap<>())
      .build();

  @SuppressWarnings("deprecation")
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    server = new MockServer();
    baseUrl = mock ? server.getBaseUrl() : BASE_URL;
    api = new Notifir(baseUrl, API_PUBLIC_KEY, API_SECRET_KEY);
  }

  @Test
  public void shouldThrowWhenBaseUrlIsNull() {
    exception.expect(NullPointerException.class);
    exception.expectMessage("The baseUrl must not be null");
    new Notifir(null, API_PUBLIC_KEY, API_SECRET_KEY);
  }

  @Test
  public void shouldThrowWhenApiPublicKeyIsNull() {
    exception.expect(NullPointerException.class);
    exception.expectMessage("The apiPublicKey must not be null");
    new Notifir(baseUrl, null, API_SECRET_KEY);
  }

  @Test
  public void shouldThrowWhenApiSecretKeyIsNull() {
    exception.expect(NullPointerException.class);
    exception.expectMessage("The apiSecretKey must not be null");
    new Notifir(baseUrl, API_PUBLIC_KEY, null);
  }

  @Test
  public void shouldThrowWhenApiPublicKeyIsInvalid() throws Exception {
    exception.expect(NotifirApiException.class);
    exception.expectMessage("Failed to authenticate using 'invalid' api key");

    Notifir notifir = new Notifir(baseUrl, "invalid", API_SECRET_KEY);
    Request<NotificationResponse> request = notifir.createNotification(testNotification);
    assertThat(request, is(notNullValue()));

    server.jsonResponse(CREATE_NOTIFICATION_401, 401);

    request.execute();
  }

  @Test
  public void shouldThrowWhenApiSecretKeyIsInvalid() throws Exception {
    exception.expect(NotifirApiException.class);
    exception.expectMessage("Failed to authenticate 'system': HMAC mismatch");

    Notifir notifir = new Notifir(baseUrl, API_PUBLIC_KEY, "invalid");
    Request<NotificationResponse> request = notifir.createNotification(testNotification);
    assertThat(request, is(notNullValue()));

    server.jsonResponse(CREATE_NOTIFICATION_401_HMAC, 401);

    request.execute();
  }

  @Test
  public void shouldPopulateRequiredHeaders() throws Exception {
    Request<NotificationResponse> request = api.createNotification(testNotification);

    assertThat(request, is(notNullValue()));

    server.jsonResponse(CREATE_NOTIFICATION_201, 201);

    NotificationResponse response = request.execute();

    RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest, hasMethodAndPath("POST", "/notifications"));
    assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));
    assertThat(recordedRequest, hasHeader("x-api-key", API_PUBLIC_KEY));
    assertThat(recordedRequest, hasHeader("x-user-id", SYSTEM_USER));
    assertThat(recordedRequest, hasHeader("x-user-hmac", "0+TZm7uauwGAqJOavzGEr8aBqUwJo3vXOb6tPxiVCYM="));
  }

  @Test
  public void shouldCreateNotification() throws Exception {
    Request<NotificationResponse> request = api.createNotification(testNotification);

    assertThat(request, is(notNullValue()));

    server.jsonResponse(CREATE_NOTIFICATION_201, 201);

    NotificationResponse response = request.execute();

    RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest, hasMethodAndPath("POST", "/notifications"));
    assertThat(recordedRequest, hasHeader("Content-Type", "application/json"));

    assertThat(response, is(notNullValue()));
    assertThat(response.getId(), is(notNullValue()));
  }

  @Test
  public void shouldThrowWhenUseWrongProjectId() throws Exception {
    exception.expect(NotifirApiException.class);
    exception.expectMessage("User system doesn't have permission to create notifications in project test");

    NotificationRequest notification = NotificationRequest
        .builder()
        .type("test")
        .recipients(Set.of("user@test.com"))
        .payload(new HashMap<>())
        .build();

    Notifir notifir = new Notifir(baseUrl, API_PUBLIC_KEY, API_SECRET_KEY);
    Request<NotificationResponse> request = notifir.createNotification(notification);
    assertThat(request, is(notNullValue()));

    server.jsonResponse(CREATE_NOTIFICATION_403, 403);

    request.execute();
  }

}
