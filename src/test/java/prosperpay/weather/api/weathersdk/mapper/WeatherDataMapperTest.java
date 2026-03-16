package prosperpay.weather.api.weathersdk.mapper;

import org.junit.jupiter.api.Test;
import prosperpay.weather.api.weathersdk.dto.WeatherApiResponse;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WeatherDataMapperTest {

    @Test
    void testMapping() {
        WeatherApiResponse response = new WeatherApiResponse();
        response.setName("London");
        response.setTimezone(3600);
        response.setVisibility(8000);
        response.setDt(1700000000L);

        WeatherApiResponse.WeatherItem item =
                new WeatherApiResponse.WeatherItem("Clouds", "broken clouds");
        response.setWeather(List.of(item));

        WeatherApiResponse.Main main = new WeatherApiResponse.Main(15.5, 14.0);
        response.setMain(main);

        WeatherApiResponse.Wind wind = new WeatherApiResponse.Wind(5.5);
        response.setWind(wind);

        WeatherApiResponse.Sys sys = new WeatherApiResponse.Sys(123L, 456L);
        response.setSys(sys);

        WeatherData data = WeatherDataMapper.INSTANCE.toWeatherData(response);

        assertEquals("London", data.getName());
        assertEquals(15.5, data.getTemperature().getTemp());
        assertEquals(14.0, data.getTemperature().getFeelsLike());
        assertEquals("Clouds", data.getWeather().getMain());
        assertEquals("broken clouds", data.getWeather().getDescription());
        assertEquals(5.5, data.getWind().getSpeed());
        assertEquals(8000, data.getVisibility());
        assertEquals(123L, data.getSys().getSunrise());
        assertEquals(456L, data.getSys().getSunset());
        assertEquals(3600, data.getTimezone());
    }
}
