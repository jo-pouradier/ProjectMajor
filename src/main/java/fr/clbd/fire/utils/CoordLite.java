package fr.clbd.fire.utils;

import com.project.model.dto.Coord;

public class CoordLite {
    
    private double lat;
    private double lon;

    public CoordLite(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public CoordLite() {
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static CoordLite fromCoord(Coord coord) {
        return new CoordLite(coord.getLat(), coord.getLon());
    }

}
