

# **Weather SDK for Java**

Java SDK for retrieving current weather data from the OpenWeather API.

---

## Features

- retrieve current weather by city name
- initialize SDK with an API key
- return weather data in a normalized JSON format
- cache recently requested cities with TTL
- support two modes:
  - **on-demand** — fetch data only when requested
  - **polling** — refresh cached data in the background
- throw descriptive exceptions on failure
- include usage examples and tests

---

Stack: Java 17, Maven, Spring Boot, Gson, Lombok, MapStruct, JUnit 5, Mockito, WireMock, JaCoCo

---


## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/OllyLicoly/weather-sdk.git
cd weather-sdk
```

### 2. Build the project

```bash
./mvnw clean install
```

On Windows:

```bash
mvnw.cmd clean install
```

### 3. Prepare API key

Create an API key in OpenWeather and pass it to the SDK from outside the source code.

For example, via environment variable:

```bash
OPENWEATHER_API_KEY=YOUR_API_KEY
```

## Configuration

The SDK is configured through `WeatherSdkConfig`.

Example:

```java
WeatherSdkConfig config = WeatherSdkConfig.builder()
        .defaultUnits(Units.METRIC)
        .cacheTtlMinutes(10)
        .pollingIntervalMinutes(0)
        .debugLogging(true)
        .build();
```

Notes:
- `pollingIntervalMinutes(0)` disables polling mode
- a positive polling interval enables background refresh
- cached entries remain valid for the configured TTL

## Example Usage

```java
String apiKey = System.getenv("OPENWEATHER_API_KEY");

WeatherSdkConfig config = WeatherSdkConfig.builder()
        .defaultUnits(Units.METRIC)
        .cacheTtlMinutes(10)
        .pollingIntervalMinutes(0)
        .debugLogging(true)
        .build();

WeatherClient client = WeatherClientFactory.create(apiKey, config);

String weatherJson = client.getCurrentWeather("Tashkent");
System.out.println(weatherJson);
```

A runnable example is available in the `examples` directory.

## Response Format

The SDK returns weather data in a normalized JSON structure like this:

```json
{
  "weather": {
    "main": "Clouds",
    "description": "scattered clouds"
  },
  "temperature": {
    "temp": 269.6,
    "feels_like": 267.57
  },
  "visibility": 10000,
  "wind": {
    "speed": 1.38
  },
  "datetime": 1675744800,
  "sys": {
    "sunrise": 1675751262,
    "sunset": 1675787560
  },
  "timezone": 3600,
  "name": "Zocca"
}
```

## Running Tests

```bash
./mvnw test
```

Generate coverage report:

```bash
./mvnw test jacoco:report
```

JaCoCo report:

```text
target/site/jacoco/index.html
```
