package notifir.hmac;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hmac {

  private static final String HMAC_SHA256 = "HmacSHA256";

  public static String calculateHMAC(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
    Mac mac = Mac.getInstance(HMAC_SHA256);
    mac.init(secretKeySpec);

    return encodeToBase64(mac.doFinal(data.getBytes()));
  }

  private static String encodeToBase64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }
}
