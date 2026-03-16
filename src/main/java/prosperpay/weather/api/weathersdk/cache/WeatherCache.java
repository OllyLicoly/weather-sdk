package prosperpay.weather.api.weathersdk.cache;

import prosperpay.weather.api.weathersdk.model.entity.WeatherData;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class WeatherCache {
    private final int maxCities;
    private final int ttlMinutes;
    private final Map<String, WeatherData> cache;

    public WeatherCache(int maxCities, int ttlMinutes) {
        this.maxCities = maxCities;
        this.ttlMinutes = ttlMinutes;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > WeatherCache.this.maxCities;
            }
        };
    }

    public synchronized WeatherData get(String city) {
        String key = normalize(city);
        WeatherData data = cache.get(key);
        if (data != null && data.isFresh()) {
            return data;
        }
        return null;
    }

    public synchronized void put(String city, WeatherData data) {
        String key = normalize(city);
        cache.put(key, data);
    }

    public synchronized Map<String, WeatherData> getAll() {
        return new LinkedHashMap<>(cache);
    }

    private String normalize(String city) {
        return city.trim().toLowerCase(Locale.ROOT);
    }
}
