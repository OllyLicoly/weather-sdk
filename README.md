

# **Weather SDK for Java**

A lightweight Java SDK for interacting with the OpenWeather API.
Provides a simple, extensible, and production-ready way to fetch and cache real-time weather data.

---

## 🚀 Features

* Easy-to-use Java client for OpenWeather API
* Configurable SDK behavior (cache, units, polling, logging)
* Built-in in-memory cache with TTL and capacity limits
* Automatic city name normalization
* Optional background polling
* Clear exceptions and error handling
* Fully covered with unit tests
* Examples included

---

## 📦 Installation

Add the dependency to your Maven project:

```xml
<dependency>
    <groupId>prosperpay.weather</groupId>
    <artifactId>weather-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```
---

## 🔧 Configuration

The SDK is configured via `WeatherSdkConfig`:

```java
WeatherSdkConfig config = WeatherSdkConfig.builder()
        .defaultUnits(Units.METRIC)
        .cacheTtlMinutes(10)
        .maxCachedCities(20)
        .pollingIntervalMinutes(0) // disable polling
        .debugLogging(true)
        .build();
```

---

## 📝 Example Program

A full runnable example is available in `/examples`.

Run:

```
mvn exec:java -Dexec.mainClass="examples.WeatherApiExample"
```

---

## 🧪 Tests

The project includes:

* Unit tests for cache
* Unit tests for WeatherClient
* WireMock integration tests for WeatherApiService
* Mockito-based tests for client behavior
* JaCoCo coverage

Run tests:

```
mvn test
```

Generate coverage report:

```
mvn test jacoco:report
```

Report will appear at:

```
target/site/jacoco/index.html
```

---

## 📚 Project Structure

```
src/
  main/java/prosperpay/weather/api/weathersdk/
    cache/
    client/
    config/
    dto/
    exception/
    mapper/
    model/entity/
    service/
  test/java/
examples/
README.md
pom.xml
```

---

## 🛠 Requirements

* Java 17+
* Maven 3.6+
* OpenWeather API key

