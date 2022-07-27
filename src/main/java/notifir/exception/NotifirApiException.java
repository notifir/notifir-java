package notifir.exception;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class NotifirApiException extends NotifirException {

  private static final String MESSAGE = "message";
  private static final String ERROR = "error";
  private static final String CODE = "code";

  private String error;
  private final String message;
  private final int statusCode;
  private Map<String, Object> values;

  public NotifirApiException(String payload, int statusCode, Throwable cause) {
    super(createMessage(payload, statusCode), cause);
    this.message = payload;
    this.statusCode = statusCode;
  }

  public NotifirApiException(Map<String, Object> values, int statusCode) {
    super(createMessage(obtainExceptionMessage(values), statusCode));
    this.values = Collections.unmodifiableMap(values);
    this.error = obtainExceptionError(this.values);
    this.message = obtainExceptionMessage(this.values);
    this.statusCode = statusCode;
  }


  private static String createMessage(String description, int statusCode) {
    return String.format("Request failed with status code %d: %s", statusCode, description);
  }

  private static String obtainExceptionMessage(Map<String, Object> values) {
    if (values.containsKey(MESSAGE)) {
      return toStringOrNull(values.get(MESSAGE));
    }
    if (values.containsKey(ERROR)) {
      return toStringOrNull(values.get(ERROR));
    }
    return "Unknown exception";
  }

  private static String obtainExceptionError(Map<String, Object> values) {
    if (values.containsKey(ERROR)) {
      return toStringOrNull(values.get(ERROR));
    }
    if (values.containsKey(CODE)) {
      return toStringOrNull(values.get(CODE));
    }
    return "Unknown error";
  }

  private static String toStringOrNull(Object obj) {
    return obj != null ? obj.toString() : null;
  }
}
