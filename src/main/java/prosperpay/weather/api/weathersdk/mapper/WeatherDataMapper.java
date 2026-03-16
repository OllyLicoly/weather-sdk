package prosperpay.weather.api.weathersdk.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import prosperpay.weather.api.weathersdk.dto.WeatherApiResponse;
import prosperpay.weather.api.weathersdk.model.entity.Weather;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;

import java.util.List;

@Mapper
public interface WeatherDataMapper {
    WeatherDataMapper INSTANCE = Mappers.getMapper(WeatherDataMapper.class);

    @Mapping(target = "weather", expression = "java(getFirstWeather(response.getWeather()))")
    @Mapping(source = "main.temp", target = "temperature.temp")
    @Mapping(source = "main.feelsLike", target = "temperature.feelsLike")
    @Mapping(source = "visibility", target = "visibility")
    @Mapping(source = "wind.speed", target = "wind.speed")
    @Mapping(source = "dt", target = "datetime")
    @Mapping(target = "timestamp", expression = "java(System.currentTimeMillis())")
    @Mapping(source = "sys.sunrise", target = "sys.sunrise")
    @Mapping(source = "sys.sunset", target = "sys.sunset")
    @Mapping(source = "timezone", target = "timezone")
    @Mapping(source = "name", target = "name")
    WeatherData toWeatherData(WeatherApiResponse response);

    default Weather getFirstWeather(List<WeatherApiResponse.WeatherItem> list) {
        if (list == null || list.isEmpty()) return null;
        WeatherApiResponse.WeatherItem item = list.get(0);
        return new Weather(item.getMain(), item.getDescription());
    }
}
