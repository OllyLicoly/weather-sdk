package prosperpay.weather.api.weathersdk.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import prosperpay.weather.api.weathersdk.model.entity.Units;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherSdkConfig {

    private int cacheTtlMinutes = 10;
    private int pollingIntervalMinutes = 5;
    private int maxCachedCities = 10;
    private Units defaultUnits = Units.METRIC;
    private boolean debugLogging = false;

    public int getCacheTtlMinutes() {
        return cacheTtlMinutes;
    }

    public WeatherSdkConfig setCacheTtlMinutes(int cacheTtlMinutes) {
        this.cacheTtlMinutes = cacheTtlMinutes;
        return this;
    }

    public int getPollingIntervalMinutes() {
        return pollingIntervalMinutes;
    }

    public WeatherSdkConfig setPollingIntervalMinutes(int pollingIntervalMinutes) {
        this.pollingIntervalMinutes = pollingIntervalMinutes;
        return this;
    }

    public int getMaxCachedCities() {
        return maxCachedCities;
    }

    public WeatherSdkConfig setMaxCachedCities(int maxCachedCities) {
        this.maxCachedCities = maxCachedCities;
        return this;
    }

    public Units getDefaultUnits() {
        return defaultUnits;
    }

    public WeatherSdkConfig setDefaultUnits(Units defaultUnits) {
        this.defaultUnits = defaultUnits;
        return this;
    }

    public boolean isDebugLogging() {
        return debugLogging;
    }

    public WeatherSdkConfig setDebugLogging(boolean debugLogging) {
        this.debugLogging = debugLogging;
        return this;
    }
}
