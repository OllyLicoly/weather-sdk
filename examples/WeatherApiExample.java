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

        // 1️⃣ API-ключ OpenWeather
        String apiKey = "3ba9fb9e55e39cc3f696e72d0f707e28";
        String apiKey1 = "240c7f4c9f48c7c46ace5740e386cf44";

        try {
            // 2️⃣ Создаём конфигурацию SDK
            WeatherSdkConfig config = WeatherSdkConfig.builder()
                    .defaultUnits(Units.METRIC)     // метрическая система (°C)
                    .pollingIntervalMinutes(0)      // polling отключён
                    .cacheTtlMinutes(10)            // TTL кэша 10 минут
                    .maxCachedCities(10)            // максимум 10 городов в кэше
                    .debugLogging(true)             // включить логирование
                    .build();

            // 3️⃣ Создаётся клиент SDK через менеджер
            WeatherClient client = WeatherManager.createClient(apiKey, false, config);

            // 4️⃣ Получаем погоду
            System.out.println("\n=== 1. On-demand запрос погоды ===");
            WeatherData la = client.getWeather("Los Angeles");
            printWeather(la);

            // 5️⃣ Проверим кэширование
            System.out.println("\n=== 2. Повторный запрос (должен быть из кэша) ===");
            WeatherData laCached = client.getWeather("  los angeles ");
            printWeather(laCached);

            // 6️⃣ Запрос другого города
            System.out.println("\n=== 3. Запрос другого города ===");
            WeatherData paris = client.getWeather("Paris");
            printWeather(paris);

            // 7️⃣ Демонстрация работы Units
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

            // 8️⃣ Пример ошибки
            System.out.println("\n=== 5. Демонстрация обработки ошибок ===");
            try {
                client.getWeather("ГородКоторогоНеСуществует123");
            } catch (WeatherException ex) {
                System.out.println("⚠ Ошибка: " + ex.getMessage());
            }

            // 9️⃣ Удаление клиента
            System.out.println("\n=== 6. Удаление клиента ===");
            WeatherManager.deleteClient(apiKey);
            System.out.println("Клиент удалён");

        } catch (WeatherException e) {
            System.err.println("Ошибка при работе с SDK: " + e.getMessage());
        }
    }

    /**
     * Удобный метод для вывода информации о погоде.
     */
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
