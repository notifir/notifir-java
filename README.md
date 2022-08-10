# Notifir Java

[![MIT][mit-badge]][mit-url]
[![Maven][maven-badge]][maven-url]
[![codecov][codecov-badge]][codecov-url]
[![Publish Stable][publish-stable-badge]][publish-stable-url]
[![Publish Unstable][publish-unstable-badge]][publish-unstable-url]

Java client library for the [Notifir](https://notifir.github.io/docs/).

## Download

Get Notifir Java via Maven:

```xml
<dependency>
  <groupId>io.github.notifir</groupId>
  <artifactId>notifir</artifactId>
  <version>0.5.6</version>
</dependency>
```

or Gradle:

```gradle
implementation 'io.github.notifir:notifir:0.5.6'
```

## Usage

### Initialization

The implementation is based on the [Notifir API](https://notifir.github.io/docs/).

Create a `Notifir` instance by providing the required details, such as `baseUrl`, `apiPublicKey` and `apiSecretKey`.

```java
Notifir auth = new Notifir(
    "{YOUR_BASE_URL}", 
    "{YOUR_API_PUBLIC_KEY}", 
    "{YOUR_API_SECRET_KEY}"
);
```

You can also customize API client using [`HttpOptions`](https://github.com/notifir/notifir-java/blob/main/src/main/java/notifir/http/HttpOptions.java) 
sent as the 4th parameter.

```java
HttpOptions httpOptions = new HttpOptions();
httpOptions.setConnectTimeout(30);

Notifir auth = new Notifir(
    "{YOUR_BASE_URL}", 
    "{YOUR_API_PUBLIC_KEY}", 
    "{YOUR_API_SECRET_KEY}",
    httpOptions
);
```

### Creating notifications

To create a [notification](https://notifir.github.io/docs/integration/api/data-model) you can use the following function:
`Request<NotificationResponse> createNotification(NotificationRequest notification)`

#### Example

```java
Notifir notifir = new Notifir(
  "https://localhost:3000/api", 
  "114ee1da-067b-11ed-be0f-6f24634ae754", 
  "114ee1da-067b-11ed-be0f-6f24634ae755"
);

try {
  NotificationRequest notification = NotificationRequest
    .builder()
    .type("test")
    .projectId("default")
    .userId("user@test.com")
    .payload(new HashMap<>())
    .build();

  NotificationResponse result = notifir.createNotification(notification).execute();
} catch (NotifirException e) {
  //Something happened
}
```

## Documentation

For more information about [Notifir](https://github.com/notifir) check our [documentation page](https://notifir.github.io/docs/).

## What is Notifir?

Notifir is created to simplify the development of in-app notifications for your product. It provides the skeleton of the notification 
system. The essential functionality is available out of the box.

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE) file for more info.


<!-- Vars -->

[mit-badge]: http://img.shields.io/:license-mit-blue.svg?style=flat
[mit-url]: https://raw.githubusercontent.com/notifir/notifir-java/main/LICENSE
[publish-stable-badge]: https://github.com/notifir/notifir-java/actions/workflows/publish-stable.yml/badge.svg
[publish-stable-url]: https://github.com/notifir/notifir-java/actions/workflows/publish-stable.yml
[publish-unstable-badge]: https://github.com/notifir/notifir-java/actions/workflows/publish-unstable.yml/badge.svg
[publish-unstable-url]: https://github.com/notifir/notifir-java/actions/workflows/publish-unstable.yml
[maven-badge]: https://img.shields.io/maven-central/v/io.github.notifir/notifir.svg
[maven-url]: http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.github.notifir%22%20AND%20a%3A%22notifir%22
[codecov-badge]: https://codecov.io/gh/notifir/notifir-java/branch/main/graph/badge.svg
[codecov-url]: https://codecov.io/gh/notifir/notifir-java