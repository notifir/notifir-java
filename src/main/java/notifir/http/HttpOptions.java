package notifir.http;

import lombok.Data;

/**
 * Used to configure additional configuration options when customizing the API client instance.
 */
@Data
public class HttpOptions {

  private int connectTimeout = 10;
  private int readTimeout = 10;
  private int maxRequests = 64;
  private int maxRequestsPerHost = 5;
  private LoggingOptions loggingOptions;

}
