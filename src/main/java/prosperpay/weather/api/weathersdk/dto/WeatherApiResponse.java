package prosperpay.weather.api.weathersdk.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherApiResponse {
    private List<WeatherItem> weather;
    private Main main;
    private Integer visibility;
    private Wind wind;
    private Long dt;
    private Sys sys;
    private Integer timezone;
    private String name;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherItem {
        private String main;
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Main {
        private Double temp;
        @SerializedName("feels_like")
        private Double feelsLike;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Wind {
        private Double speed;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sys {
        private Long sunrise;
        private Long sunset;
    }
}
