package prosperpay.weather.api.weathersdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import prosperpay.weather.api.weathersdk.client.WeatherClient;
import prosperpay.weather.api.weathersdk.config.WeatherSdkConfig;
import prosperpay.weather.api.weathersdk.exception.WeatherException;
import prosperpay.weather.api.weathersdk.model.entity.Units;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherManagerTest {

    WeatherSdkConfig config;

    @BeforeEach
    void setUp() {
        config = WeatherSdkConfig.builder()
                .defaultUnits(Units.METRIC)
                .pollingIntervalMinutes(0)
                .cacheTtlMinutes(10)
                .maxCachedCities(10)
                .debugLogging(false)
                .build();

        // очистка менеджера через delete
        try {
            WeatherManager.deleteClient("key1");
        } catch (Exception ignored) {}
    }

    @Test
    void testCreateClient() throws WeatherException {
        WeatherClient client = WeatherManager.createClient("key1", false, config);

        assertNotNull(client);
    }

    @Test
    void testDuplicateKeyThrows() throws WeatherException {
        WeatherManager.createClient("key1", false, config);

        assertThrows(WeatherException.class, () ->
                WeatherManager.createClient("key1", true, config)
        );
    }

    @Test
    void testDeleteClient() throws WeatherException {
        WeatherManager.createClient("key1", false, config);
        WeatherManager.deleteClient("key1");

        assertThrows(WeatherException.class, () ->
                WeatherManager.deleteClient("key1")
        );
    }
}
