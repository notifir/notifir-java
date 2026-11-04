package notifir;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Map;

@Value
@Builder(toBuilder = true)
public class NotificationRequest implements Serializable {
  String type;
  String actionUrl;
  String recipient;
  Map<String, Object> payload;
}
