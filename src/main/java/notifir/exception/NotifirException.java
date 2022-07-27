package notifir.exception;

import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class NotifirException extends IOException {

  public NotifirException(String message) {
    super(message);
  }

  public NotifirException(String message, Throwable cause) {
    super(message, cause);
  }
}
