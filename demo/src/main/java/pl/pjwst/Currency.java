package pl.pjwst;

import java.util.List;

public class Currency {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}

/*
Table – typ tabeli
No – numer tabeli
TradingDate – data notowania (dotyczy tabeli C)
EffectiveDate – data publikacji
Rates – lista kursów poszczególnych walut w tabeli
Country – nazwa kraju
Symbol – symbol waluty (numeryczny, dotyczy kursów archiwalnych)
Currency – nazwa waluty
Code – kod waluty
Bid – przeliczony kurs kupna waluty (dotyczy tabeli C)
Ask – przeliczony kurs sprzedaży waluty (dotyczy tabeli C)
Mid – przeliczony kurs średni waluty (dotyczy tabel A oraz B)
 */