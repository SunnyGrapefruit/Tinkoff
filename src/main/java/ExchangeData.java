import com.google.gson.annotations.SerializedName;

public class ExchangeData {
    @SerializedName("Date")
    private String date;
    @SerializedName("Timestamp")
    private String timestamp;
    @SerializedName("Valute")
    private Valute valute;

    public Valute getValute() {
        return valute;
    }

    public class Valute {
        @SerializedName("USD")
        private ValuteData usd;
        @SerializedName("EUR")
        private ValuteData eur;

        public ValuteData getEur() {
            return eur;
        }

        public ValuteData getUsd() {
            return usd;
        }
    }

    public class ValuteData {
        @SerializedName("Value")
        private Double value;

        public Double getValue() {
            return value;
        }
    }
}

