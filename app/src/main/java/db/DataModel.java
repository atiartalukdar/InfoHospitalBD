package db;

/**
 * Created by Atiar on 12/4/17.
 */

public class DataModel {
    String transportName;
    String busStop;
    String lat;
    String lang;
    String bnName;

    public DataModel(String transportName, String busStop, String lat, String lang, String bnName) {
        this.transportName = transportName;
        this.busStop = busStop;
        this.lat = lat;
        this.lang = lang;
        this.bnName = bnName;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getBusStop() {
        return busStop;
    }

    public void setBusStop(String busStop) {
        this.busStop = busStop;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getBnName() {
        return bnName;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }
}
