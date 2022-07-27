package notifir;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder(toBuilder = true)
public class NotificationRequest {
  String type;
  String projectId;
  String userId;
  Map<String, Object> payload;
}
