package notifir.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import notifir.exception.NotifirApiException;
import notifir.exception.NotifirException;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequest<T> implements Request<T> {

  private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

  private final OkHttpClient client;
  private final String url;
  private final String method;
  private final ObjectMapper mapper;
  private final Map<String, String> headers;

  BaseRequest(OkHttpClient client, String url, String method, ObjectMapper mapper) {
    this.client = client;
    this.url = url;
    this.method = method;
    this.mapper = mapper;
    this.headers = new HashMap<>();
  }

  protected okhttp3.Request createRequest() throws NotifirException {
    RequestBody body;
    try {
      body = this.createRequestBody();
    } catch (IOException e) {
      throw new NotifirException("Couldn't create the request body.", e);
    }
    okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
        .url(url)
        .method(method, body);
    for (Map.Entry<String, String> e : headers.entrySet()) {
      builder.addHeader(e.getKey(), e.getValue());
    }
    builder.addHeader("Content-Type", getContentType());
    return builder.build();
  }

  protected T parseResponse(Response response) throws NotifirException {
    if (!response.isSuccessful()) {
      throw createResponseException(response);
    }

    try (ResponseBody body = response.body()) {
      return readResponseBody(body);
    } catch (IOException e) {
      throw new NotifirApiException("Failed to parse the response body.", response.code(), e);
    }
  }

  /**
   * Getter for the content-type header value to use on this request
   *
   * @return the content-type
   */
  protected String getContentType() {
    return CONTENT_TYPE_APPLICATION_JSON;
  }

  /**
   * Responsible for creating the payload that will be set as body on this request.
   *
   * @return the body to send as part of the request.
   * @throws IOException if an error is raised during the creation of the body.
   */
  protected abstract RequestBody createRequestBody() throws IOException;

  /**
   * Responsible for parsing the payload that is received as part of the response.
   *
   * @param body the received body payload. The body buffer will automatically closed.
   * @return the instance of type T, result of interpreting the payload.
   * @throws IOException if an error is raised during the parsing of the body.
   */
  protected abstract T readResponseBody(ResponseBody body) throws IOException;

  /**
   * Adds an HTTP header to the request
   *
   * @param name  the name of the header
   * @param value the value of the header
   * @return this same request instance
   */
  public BaseRequest<T> addHeader(String name, String value) {
    headers.put(name, value);
    return this;
  }

  /**
   * Responsible for parsing an unsuccessful request (status code other than 200)
   * and generating a developer-friendly exception with the error details.
   *
   * @param response the unsuccessful response, as received. If its body is accessed, the buffer must be closed.
   * @return the exception with the error details.
   */
  protected NotifirException createResponseException(Response response) {
    String payload = null;
    try (ResponseBody body = response.body()) {
      payload = body.string();
      MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
      Map<String, Object> values = mapper.readValue(payload, mapType);
      return new NotifirApiException(values, response.code());
    } catch (IOException e) {
      return new NotifirApiException(payload, response.code(), e);
    }
  }

  /**
   * Executes this request.
   *
   * @return the response body JSON decoded as T
   * @throws NotifirException if the request execution fails.
   */
  @Override
  public T execute() throws NotifirException {
    okhttp3.Request request = createRequest();
    try (Response response = client.newCall(request).execute()) {
      return parseResponse(response);
    } catch (NotifirException e) {
      throw e;
    } catch (IOException e) {
      throw new NotifirException("Failed to execute request", e);
    }
  }

}