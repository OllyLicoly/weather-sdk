package prosperpay.weather.api.weathersdk.model.entity;

public enum Units {
    METRIC("metric"),
    IMPERIAL("imperial");

    private final String apiName;

    Units(String apiName) {
        this.apiName = apiName;
    }

    public String getApiName() {
        return apiName;
    }
}
