package prosperpay.weather.api.weathersdk.exception;

public class WeatherException extends Exception {
    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
