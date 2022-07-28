package notifir;

import static notifir.hmac.Hmac.calculateHMAC;

import com.fasterxml.jackson.core.type.TypeReference;
import notifir.http.CustomRequest;
import notifir.http.HttpOptions;
import notifir.http.LoggingOptions;
import notifir.http.Request;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.Validate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class Notifir {

  public static final String HEADER_X_API_KEY = "x-api-key";
  public static final String HEADER_X_USER_ID = "x-user-id";
  public static final String HEADER_X_USER_HMAC = "x-user-hmac";

  public static final String SYSTEM_USER = "system";

  private final OkHttpClient client;
  private final String apiPublicKey;
  private final String apiSecretKey;
  private final HttpUrl baseUrl;
  private final HttpLoggingInterceptor logging;

  /**
   * Create a new instance with the given api public and secret keys.
   * In addition, accepts an {@link HttpOptions} that will be used to configure the networking client.
   *
   * @param baseUrl       API base url.
   * @param apiPublicKey  API public key.
   * @param apiSecretKey  API secret key.
   * @param options       Configuration options for this client instance.
   * @see #Notifir(String, String, String)
   */
  public Notifir(String baseUrl, String apiPublicKey, String apiSecretKey, HttpOptions options) {

    Validate.notNull(baseUrl, "The baseUrl must not be null");
    Validate.notNull(apiPublicKey, "The apiPublicKey must not be null");
    Validate.notNull(apiSecretKey, "The apiSecretKey must not be null");

    this.baseUrl = HttpUrl.parse(baseUrl);
    this.apiPublicKey = apiPublicKey;
    this.apiSecretKey = apiSecretKey;

    logging = new HttpLoggingInterceptor();
    client = buildNetworkingClient(options);
  }

  /**
   * Create a new instance with the given base url, api public key and api secret key.
   *
   * @param baseUrl       API base url.
   * @param apiPublicKey  API public key.
   * @param apiSecretKey  API secret key.
   */
  public Notifir(String baseUrl, String apiPublicKey, String apiSecretKey) {
    this(baseUrl, apiPublicKey, apiSecretKey, new HttpOptions());
  }

  /**
   * Create notification
   * i.e.:
   * <pre>
   * {@code
   * Notifir notifir = new Notifir("https://localhost:3000/api", "114ee1da-067b-11ed-be0f-6f24634ae754", "114ee1da-067b-11ed-be0f-6f24634ae755");
   * try {
   *      NotificationRequest notification = new NotificationRequest();
   *      NotificationResponse result = notifir.createNotification(notification).execute();
   * } catch (NotifirException e) {
   *      //Something happened
   * }
   * }
   * </pre>
   *
   * @param notification notification
   * @return a Request to execute.
   */
  public Request<NotificationResponse> createNotification(NotificationRequest notification) throws NoSuchAlgorithmException,
      InvalidKeyException {
    Validate.notNull(notification, "The notification must not be null");

    String url = baseUrl
        .newBuilder()
        .addPathSegment("notifications")
        .build()
        .toString();

    CustomRequest<NotificationResponse> request = new CustomRequest<>(
        client, url, "POST", new TypeReference<NotificationResponse>() {}
    );
    request.setBody(notification);

    request.addHeader(HEADER_X_API_KEY, apiPublicKey);
    request.addHeader(HEADER_X_USER_ID, SYSTEM_USER);
    request.addHeader(HEADER_X_USER_HMAC, calculateHMAC(SYSTEM_USER, apiSecretKey));

    return request;
  }



  /**
   * Given a set of options, it creates a new instance of the {@link OkHttpClient}
   * configuring them according to their availability.
   *
   * @param options the options to set to the client.
   * @return a new networking client instance configured as requested.
   */
  private OkHttpClient buildNetworkingClient(HttpOptions options) {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

    configureLogging(options.getLoggingOptions());
    Dispatcher dispatcher = new Dispatcher();
    dispatcher.setMaxRequests(options.getMaxRequests());
    dispatcher.setMaxRequestsPerHost(options.getMaxRequestsPerHost());
    return clientBuilder
        .addInterceptor(logging)
        .connectTimeout(options.getConnectTimeout(), TimeUnit.SECONDS)
        .readTimeout(options.getReadTimeout(), TimeUnit.SECONDS)
        .dispatcher(dispatcher)
        .build();
  }

  private void configureLogging(LoggingOptions loggingOptions) {
    if (loggingOptions == null) {
      logging.setLevel(HttpLoggingInterceptor.Level.NONE);
      return;
    }
    switch (loggingOptions.getLogLevel()) {
      case BASIC:
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        break;
      case HEADERS:
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        break;
      case BODY:
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        break;
      case NONE:
      default:
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
    }
    for (String header : loggingOptions.getHeadersToRedact()) {
      logging.redactHeader(header);
    }
  }

}
