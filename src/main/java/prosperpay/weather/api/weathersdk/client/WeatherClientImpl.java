package prosperpay.weather.api.weathersdk.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosperpay.weather.api.weathersdk.cache.WeatherCache;
import prosperpay.weather.api.weathersdk.config.WeatherSdkConfig;
import prosperpay.weather.api.weathersdk.exception.WeatherException;
import prosperpay.weather.api.weathersdk.model.entity.Units;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;
import prosperpay.weather.api.weathersdk.service.WeatherApiService;
import prosperpay.weather.api.weathersdk.service.WeatherPollingService;

import java.util.Locale;

public class WeatherClientImpl implements WeatherClient{

    private static final Logger log = LoggerFactory.getLogger(WeatherClientImpl.class);
    private final WeatherApiService apiService;
    private final WeatherCache cache;
    private final WeatherPollingService pollingService;
    private final WeatherSdkConfig config;
    private final boolean pollingMode;

    public WeatherClientImpl(String apiKey, boolean pollingMode, Units units, WeatherSdkConfig config) throws WeatherException {
        if (apiKey == null || apiKey.isBlank()) {
            throw new WeatherException("API ключ не может быть пустым.");
        }

        this.pollingMode = pollingMode;
        this.config = config;
        this.apiService = new WeatherApiService(apiKey, units);
        this.cache = new WeatherCache(config.getMaxCachedCities(),config.getCacheTtlMinutes());
        this.pollingService = new WeatherPollingService(apiService, cache, config.getPollingIntervalMinutes());

        if (config.isDebugLogging()) {
            log.info("Debug logging enabled");
        }

        if (pollingMode && config.getPollingIntervalMinutes() > 0) {
            log.info("Starting polling mode (interval={} min)", config.getPollingIntervalMinutes());
            pollingService.start();
        }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    pollingService.stop();
                } catch (Exception ignored) {}
            }));

    }

    @Override
    public WeatherData getWeather(String city) throws WeatherException {
        if (city == null || city.isBlank()) {
            throw new WeatherException("Название города не может быть пустым.");
        }

        log.info("WeatherClient: getWeather('{}') called", city);
        String normalizedCity = city.trim().toLowerCase(Locale.ROOT);
        log.debug("CACHE LOOKUP for city={}", normalizedCity);
        WeatherData cached = cache.get(normalizedCity);
        if (cached != null) {
            log.info("CACHE HIT for city={}", normalizedCity);
            return cached;
        }

        if (pollingMode) {
            throw new WeatherException("Данные ещё не готовы. Подождите, пока polling обновит погоду.");
        }

        log.info("CACHE MISS for city={} → calling API", normalizedCity);
        WeatherData data = apiService.fetchWeather(city);
        log.debug("CACHE PUT city={} (timestamp={})", normalizedCity, data.getTimestamp());
        cache.put(normalizedCity, data);
        return data;
    }

    @Override
    public void stopPolling() {
        pollingService.stop();
    }
}
