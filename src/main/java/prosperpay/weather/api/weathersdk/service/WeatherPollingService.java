package prosperpay.weather.api.weathersdk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosperpay.weather.api.weathersdk.cache.WeatherCache;
import prosperpay.weather.api.weathersdk.exception.WeatherException;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherPollingService {
    private static final Logger log = LoggerFactory.getLogger(WeatherPollingService.class);
    private final WeatherApiService apiService;
    private final WeatherCache cache;
    private final int intervalMinutes;
    private ScheduledExecutorService scheduler;

    public WeatherPollingService(WeatherApiService apiService, WeatherCache cache, int intervalMinutes) {
        this.apiService = apiService;
        this.cache = cache;
        this.intervalMinutes = intervalMinutes;
    }

    public void start() {
        if (intervalMinutes <= 0) {
            log.warn("Polling not started: intervalMinutes={}", intervalMinutes);
            return;
        }

        log.info("WeatherPollingService started (interval={} min)", intervalMinutes);

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {

            try {
                Map<String, WeatherData> all = cache.getAll();

                for (Map.Entry<String, WeatherData> entry : all.entrySet()) {
                    String cacheKey = entry.getKey();
                    WeatherData data = entry.getValue();

                    String cityForApi = data.getName();

                    log.debug("Polling update for city={} (API name={})", cacheKey, cityForApi);

                    WeatherData updated = apiService.fetchWeather(cityForApi);
                    cache.put(cacheKey, updated);
                }

            } catch (WeatherException e) {
                log.error("Polling update failed: {}", e.getMessage(), e);
            }

        }, 0, intervalMinutes, TimeUnit.MINUTES);
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            log.info("WeatherPollingService stopped");
            scheduler.shutdownNow();
        }
    }
}
