package prosperpay.weather.api.weathersdk.client;

import prosperpay.weather.api.weathersdk.exception.WeatherException;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;

public interface WeatherClient {
    WeatherData getWeather(String city) throws WeatherException;
    void stopPolling();
}
