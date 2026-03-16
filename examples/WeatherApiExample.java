import prosperpay.weather.api.weathersdk.WeatherManager;
import prosperpay.weather.api.weathersdk.client.WeatherClient;
import prosperpay.weather.api.weathersdk.config.WeatherSdkConfig;
import prosperpay.weather.api.weathersdk.exception.WeatherException;
import prosperpay.weather.api.weathersdk.model.entity.Units;
import prosperpay.weather.api.weathersdk.model.entity.WeatherData;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeatherApiExample {    

    public static void main(String[] args) {

        String apiKey = "YOUR_API_KEY";
        String apiKey1 = "YOUR_API_KEY_1";

        try {
            WeatherSdkConfig config = WeatherSdkConfig.builder()
                    .defaultUnits(Units.METRIC)
                    .pollingIntervalMinutes(0)
                    .cacheTtlMinutes(10)
                    .maxCachedCities(10)
                    .debugLogging(true)
                    .build();

            WeatherClient client = WeatherManager.createClient(apiKey, false, config);

            System.out.println("\n=== 1. On-demand запрос погоды ===");
            WeatherData la = client.getWeather("Los Angeles");
            printWeather(la);

            System.out.println("\n=== 2. Повторный запрос (должен быть из кэша) ===");
            WeatherData laCached = client.getWeather("  los angeles ");
            printWeather(laCached);

            System.out.println("\n=== 3. Запрос другого города ===");
            WeatherData paris = client.getWeather("Paris");
            printWeather(paris);

            System.out.println("\n=== 4. Использование Units.IMPERIAL ===");
            WeatherSdkConfig configImperial = WeatherSdkConfig.builder()
                    .defaultUnits(Units.IMPERIAL)
                    .pollingIntervalMinutes(0)
                    .cacheTtlMinutes(10)
                    .maxCachedCities(10)
                    .debugLogging(true)
                    .build();

            WeatherClient clientImperial =
                    WeatherManager.createClient(apiKey1, false, configImperial);

            WeatherData nyF = clientImperial.getWeather("New York");
            printWeather(nyF);

            System.out.println("\n=== 5. Демонстрация обработки ошибок ===");
            try {
                client.getWeather("ГородКоторогоНеСуществует123");
            } catch (WeatherException ex) {
                System.out.println("⚠ Ошибка: " + ex.getMessage());
            }

            System.out.println("\n=== 6. Удаление клиента ===");
            WeatherManager.deleteClient(apiKey);
            System.out.println("Клиент удалён");

        } catch (WeatherException e) {
            System.err.println("Ошибка при работе с SDK: " + e.getMessage());
        }
    }

    private static void printWeather(WeatherData data) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = Instant.ofEpochMilli(data.getTimestamp())
                .atZone(ZoneId.systemDefault())
                .format(fmt);

        System.out.println("City:        " + data.getName());
        System.out.println("Temp:        " + data.getTemperature().getTemp());
        System.out.println("Feels Like:  " + data.getTemperature().getFeelsLike());
        System.out.println("Weather:     " + data.getWeather().getMain()
                + " (" + data.getWeather().getDescription() + ")");
        System.out.println("Wind Speed:  " + data.getWind().getSpeed());
        System.out.println("Visibility:  " + data.getVisibility());
        System.out.println("Timestamp:   " + timestamp);
    }
}
