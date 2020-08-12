package com.exchange.dto.request;


public class TradeDownloadRequest {

    /**
     * Unix TimeStamp in MilliSeconds
     */
    private long fromDate;

    /**
     * Unix TimeStamp in MilliSeconds
     */
    private long toDate;

    /**
     * Arr [PriceCurrency_SizeCurrency]
     */
    private String symbols;

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }
}
