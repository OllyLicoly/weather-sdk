package prosperpay.weather.api.weathersdk.exception;

//Почему WeatherException наследуется от Exception, а не от RuntimeException?

//Потому что это checked exception — то есть ошибка, которую вызывающий код обязан обработать (например, при сетевом запросе).
//Это логические, ожидаемые ошибки, а не сбои исполнения, поэтому их лучше явно указывать в throws.

public class WeatherException extends Exception {
    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
