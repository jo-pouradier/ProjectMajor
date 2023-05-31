package fr.clbd.fire.utils;

import com.project.model.dto.Coord;
import com.project.tools.GisTools;

public class GeoUtils {

    public static Coord addNMetters() {
        return null;
    }

    public static double getDistance(Coord a, Coord b) {
        double R = 6372.795477598;
        double distance = R * Math.acos(Math.sin(Math.toRadians(a.getLat())) * Math.sin(Math.toRadians(b.getLat())) + Math.cos(Math.toRadians(a.getLat())) * Math.cos(Math.toRadians(b.getLat())) * Math.cos(Math.toRadians(a.getLon() - b.getLon())));
        return distance;
    }
}
