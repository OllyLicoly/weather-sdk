package prosperpay.weather.api.weathersdk.cache;

import org.junit.jupiter.api.Test;
import prosperpay.weather.api.weathersdk.model.entity.Temperature;
import prosperpay.weather.api.weathersdk.model.entity.Weather;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeatherCacheTest {
    @Test
    void testPutAndGet() {
        WeatherCache cache = new WeatherCache(10, 10);
        WeatherData data = new WeatherData(
                new Weather("Clouds", "scattered"),
                new Temperature(10.1, 8.3),
                1000, null, 0L, null, 0, "Paris", System.currentTimeMillis()
        );

        cache.put("Paris", data);
        WeatherData cached = cache.get("Paris");

        assertNotNull(cached);
        assertEquals("Paris", cached.getName());
    }

    @Test
    void testNormalization() {
        WeatherCache cache = new WeatherCache(10, 10);
        WeatherData data = new WeatherData();

        cache.put("  PaRiS  ", data);
        assertNotNull(cache.get("paris"));
        assertNotNull(cache.get("PARIS"));
    }

    @Test
    void testWeatherDataExpiration() {
        WeatherData data = new WeatherData();

        // свежий
        assertTrue(data.isFresh());

        // старим на 20 минут
        data.setTimestamp(System.currentTimeMillis() - 20 * 60 * 1000);

        assertFalse(data.isFresh());
    }

    @Test
    void testEviction() {
        WeatherCache cache = new WeatherCache(3, 10);

        cache.put("A", new WeatherData());
        cache.put("B", new WeatherData());
        cache.put("C", new WeatherData());
        cache.put("D", new WeatherData()); // A должна быть удалена

        assertNull(cache.get("A"));
        assertNotNull(cache.get("B"));
        assertNotNull(cache.get("C"));
        assertNotNull(cache.get("D"));
    }

    @Test
    void testGetAllReturnsCopy() {
        WeatherCache cache = new WeatherCache(10, 10);
        cache.put("Paris", new WeatherData());

        var map = cache.getAll();
        map.clear();

        assertFalse(cache.getAll().isEmpty());
    }
}
