package notifir.http;

import notifir.exception.NotifirException;

/**
 * Class that represents an HTTP Request that can be executed.
 *
 * @param <T> the type of payload expected in the response after the execution.
 */
public interface Request<T> {

  /**
   * Executes this request synchronously.
   *
   * @return the response body JSON decoded as T
   * @throws NotifirException if the request couldn't be created or executed successfully.
   */
  T execute() throws NotifirException;

}
