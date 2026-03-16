package prosperpay.weather.api.weathersdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prosperpay.weather.api.weathersdk.client.WeatherClient;
import prosperpay.weather.api.weathersdk.client.WeatherClientImpl;
import prosperpay.weather.api.weathersdk.config.WeatherSdkConfig;
import prosperpay.weather.api.weathersdk.exception.WeatherException;

import java.util.concurrent.ConcurrentHashMap;

public class WeatherManager {
    private static final Logger log = LoggerFactory.getLogger(WeatherManager.class);
    private static final ConcurrentHashMap<String, WeatherClientImpl> clients = new ConcurrentHashMap<>();


    public static synchronized WeatherClientImpl createClient(String apiKey, boolean pollingMode, WeatherSdkConfig config) throws WeatherException {
        if (apiKey == null || apiKey.isBlank()) {
            throw new WeatherException("API ключ не может быть пустым.");
        }

        if (clients.containsKey(apiKey)) {
            throw new WeatherException("SDK с этим API-ключом уже существует.");
        }
        log.info("Creating WeatherClient for key={} (polling={}, units={})", apiKey, pollingMode, config.getDefaultUnits());
        WeatherClientImpl client = new WeatherClientImpl(apiKey, pollingMode, config.getDefaultUnits(), config);
        clients.put(apiKey, client);
        return client;
    }

    public static synchronized void deleteClient(String apiKey) throws WeatherException {
        if (apiKey == null || apiKey.isBlank()) {
            throw new WeatherException("API ключ не может быть пустым.");
        }

        WeatherClient removed = clients.remove(apiKey);

        if (removed == null) {
            throw new WeatherException("SDK с таким API-ключом не найден.");
        }

        log.info("Deleting WeatherClient for key={}", apiKey);

        removed.stopPolling();
    }
}
