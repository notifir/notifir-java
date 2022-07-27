package notifir.http;

/**
 * A request class that can be customized in different ways.
 * i.e. setting headers, body parameters, etc.
 *
 * @param <T> The type expected to be received as part of the response.
 */
@SuppressWarnings("UnusedReturnValue")
interface CustomizableRequest<T> extends Request<T> {

  /**
   * Adds an HTTP header to the request
   *
   * @param name  the name of the header
   * @param value the value of the header
   * @return this same request instance
   */
  CustomizableRequest<T> addHeader(String name, String value);

  /**
   * Sets the response's body directly
   *
   * @param body the value to set as body
   * @return this same request instance
   */
  CustomizableRequest<T> setBody(Object body);
}
