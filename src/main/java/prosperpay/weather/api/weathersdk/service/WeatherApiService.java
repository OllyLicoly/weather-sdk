package prosperpay.weather.api.weathersdk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosperpay.weather.api.weathersdk.client.WeatherClientImpl;
import prosperpay.weather.api.weathersdk.dto.WeatherApiResponse;
import prosperpay.weather.api.weathersdk.exception.WeatherException;
import prosperpay.weather.api.weathersdk.mapper.WeatherDataMapper;
import prosperpay.weather.api.weathersdk.model.entity.Units;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherApiService {
    private static final Logger log = LoggerFactory.getLogger(WeatherApiService.class);
    private final String apiKey;
    private final Units units;
    private final Gson gson = new Gson();

    public WeatherApiService(String apiKey, Units units) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API ключ не может быть пустым");
        }
        this.apiKey = apiKey;
        this.units = units == null ? Units.METRIC : units;
    }

    /**
     * Получает данные о погоде для заданного города.
     */
    public WeatherData fetchWeather(String city) throws WeatherException {
        try {
            // Кодируем название города
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            log.debug("Preparing API request for city={}", city);
            String urlStr = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s",
                    encodedCity, apiKey, units.getApiName()
            );

            URL url = new URL(urlStr);
            log.info("HTTP GET {}", urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();
            log.info("API returned code {}", code);
            if (code != 200) {
                try (BufferedReader errReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    while ((line = errReader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                throw new WeatherException("Ошибка API: код " + code);
            }

            // Парсим JSON
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                log.debug("Parsing API response for city={}", city);
                WeatherApiResponse response = gson.fromJson(reader, WeatherApiResponse.class);
                return WeatherDataMapper.INSTANCE.toWeatherData(response);
            }

        } catch (Exception e) {
            throw new WeatherException("Не удалось получить данные о погоде: " + e.getMessage(), e);
        }
    }
}
