package prosperpay.weather.api.weathersdk.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prosperpay.weather.api.weathersdk.config.WeatherSdkConfig;
import prosperpay.weather.api.weathersdk.exception.WeatherException;
import prosperpay.weather.api.weathersdk.model.entity.Units;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;
import prosperpay.weather.api.weathersdk.service.WeatherApiService;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WeatherClientImplTest {

    @Mock
    private WeatherApiService apiMock;

    private WeatherSdkConfig config;
    private WeatherClientImpl client;

    @BeforeEach
    void setup() throws Exception {
        config = WeatherSdkConfig.builder()
                .maxCachedCities(10)
                .cacheTtlMinutes(10)
                .pollingIntervalMinutes(0)   // polling OFF
                .defaultUnits(Units.METRIC)
                .debugLogging(false)
                .build();

        // Создаём объект
        client = new WeatherClientImpl(
                "key",
                false,            // pollingMode
                Units.METRIC,
                config
        );

        // === Подмена apiService на мок через Reflection ===
        Field apiServiceField = WeatherClientImpl.class.getDeclaredField("apiService");
        apiServiceField.setAccessible(true);
        apiServiceField.set(client, apiMock);
    }

    /**
     * 🔹 API вызывается 1 раз → второй раз из кеша
     */
    @Test
    void testCaching() throws Exception {
        WeatherData paris = new WeatherData();
        when(apiMock.fetchWeather(anyString())).thenReturn(paris);

        client.getWeather("Paris");        // API CALL
        client.getWeather("   PARIS  ");   // CACHE

        // API должен быть вызван ровно один раз
        verify(apiMock, times(1)).fetchWeather(anyString());
    }

    /**
     * 🔹 Нормализация названий городов
     */
    @Test
    void testCityNormalization() throws Exception {
        WeatherData data = new WeatherData();
        when(apiMock.fetchWeather(anyString())).thenReturn(data);

        client.getWeather("  LoNDoN ");  // API CALL
        client.getWeather("london");     // CACHE
        client.getWeather("LONDON");     // CACHE

        verify(apiMock, times(1)).fetchWeather(anyString());
    }

    /**
     * 🔹 В polling режиме прямой вызов запрещён
     */
    @Test
    void testPollingModeBlocksApiCall() throws Exception {
        WeatherSdkConfig pollingCfg = WeatherSdkConfig.builder()
                .maxCachedCities(10)
                .cacheTtlMinutes(10)
                .pollingIntervalMinutes(5) // === polling включен ===
                .defaultUnits(Units.METRIC)
                .debugLogging(false)
                .build();

        WeatherClientImpl pollingClient = new WeatherClientImpl(
                "key2",
                true,
                Units.METRIC,
                pollingCfg
        );

        assertThrows(
                WeatherException.class,
                () -> pollingClient.getWeather("Berlin")
        );
    }

    /**
     * 🔹 API вызывается, если данных нет в кеше
     */
    @Test
    void testApiCalledWhenCacheEmpty() throws Exception {
        WeatherData data = new WeatherData();
        when(apiMock.fetchWeather(anyString())).thenReturn(data);

        client.getWeather("Rome");

        verify(apiMock, times(1)).fetchWeather(anyString());
    }
}
