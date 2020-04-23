import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ExchangeData {
    @SerializedName("Date")
    private Date date;
    @SerializedName("Timestamp")
    private Date timestamp;
    @SerializedName("Valute")
    private Valute valute;

    public Valute getValute() {
        return valute;
    }

    public Date getDate() {
        return date;
    }

    public Date getTimestamp() {
        return timestamp;
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

