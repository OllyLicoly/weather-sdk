package prosperpay.weather.api.weathersdk.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WeatherData {
    private Weather weather;
    private Temperature temperature;
    private Integer visibility;
    private Wind wind;
    private Long datetime;
    private Sys sys;
    private Integer timezone;
    private String name;
    private Long timestamp;

    public WeatherData() {
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isFresh() {
        return (System.currentTimeMillis() - timestamp) < (10 * 60 * 1000);
    }

}
