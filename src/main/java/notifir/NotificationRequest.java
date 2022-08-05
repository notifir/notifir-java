package notifir;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class NotificationRequest implements Serializable {
  String type;
  String actionUrl;
  Set<String> recipients;
  Map<String, Object> payload;
}
