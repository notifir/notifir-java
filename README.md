# Notifir Java

[![MIT][mit-badge]][mit-url]

Java client library for the [Notifir](https://notifir.github.io/docs/).

## Download

Get Notifir Java via Maven:

```xml
<dependency>
  <groupId>io.github.notifir</groupId>
  <artifactId>notifir</artifactId>
  <version>0.1.0</version>
</dependency>
```

or Gradle:

```gradle
implementation 'io.github.notifir:notifir:0.1.0'
```

## Usage

### Initialization

The implementation is based on the [Notifir API](https://notifir.github.io/docs/).

Create an `Notifir` instance by providing the required details, such as `baseUrl`, `apiPublicKey` and `apiSecretKey`.

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




<!-- Vars -->

[mit-badge]: http://img.shields.io/:license-mit-blue.svg?style=flat
[mit-url]: https://raw.githubusercontent.com/notifir/notifir-java/main/LICENSE


