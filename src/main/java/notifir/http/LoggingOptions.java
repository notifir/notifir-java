package notifir.http;

import lombok.Data;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.Set;

/**
 * Used to configure the HTTP Logging options.
 */
@Data
public class LoggingOptions {

  public enum LogLevel {

    /**
     * No logging.
     */
    NONE,

    /**
     * Logs request and response lines.
     */
    BASIC,

    /**
     * Logs request and response lines, along with their respective headers. Note that headers may contain
     * sensitive information; see {@linkplain #headersToRedact}
     */
    HEADERS,

    /**
     * Logs request and response lines, along with their respective headers and bodies. Note that headers and bodies
     * may contain sensitive information; see {@linkplain #headersToRedact} for header redaction, but that only
     * applies to headers. This should only be used in controlled or non-production environments.
     */
    BODY
  }

  private LogLevel logLevel;
  private Set<String> headersToRedact = Collections.emptySet();

  /**
   * Create a new instance using the specified {@linkplain LogLevel}
   * @param logLevel the log level to set. Must not be null.
   */
  public LoggingOptions(LogLevel logLevel) {
    Validate.notNull(logLevel, "The logLevel must not be null");
    this.logLevel = logLevel;
  }

}
