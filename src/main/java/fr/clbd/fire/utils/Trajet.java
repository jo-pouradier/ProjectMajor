package fr.clbd.fire.utils;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import fr.clbd.fire.utils.google.LatLongDto;
import fr.clbd.fire.utils.google.StepDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Trajet {

    private int id;
    private float distance;
    private StepDto[] steps;
    private List<Coord> coords;
    private List<Float> distanceCumulee;


    public Trajet(int id, Coord start, Coord end) {
        this.id = id;
        this.steps = RequestsUtils.getRoute(new Coord(start.getLon(), start.getLat()), new Coord(end.getLon(), end.getLat()));
        this.distanceCumulee = new ArrayList<>();
        this.distanceCumulee.add(0.0f);
        this.coords = new ArrayList<>();
        int i = 1;
        for (StepDto step : this.steps) {
            List<Coord> intermediaire = RequestsUtils.decodePoly(step.getPolyline().getEncodedPolyline()).stream().map(LatLongDto::toCoord).collect(Collectors.toList());
            this.coords.addAll(intermediaire);
            this.coords.add(new Coord(step.getEndLocation().getLatLong().getLongitude(), step.getEndLocation().getLatLong().getLatitude()));
//            this.distanceCumulee.add(this.distanceCumulee.get(i - 1) +
//                    step.getDistanceMeters());
            i++;
        }
        for (i = 1; i < this.coords.size(); i++) {
            distanceCumulee.add(distanceCumulee.get(i-1)+(float) (1000*RequestsUtils.getDistanceBetweenCoord(this.coords.get(i - 1), this.coords.get(i))));
        }
        this.distance = RequestsUtils.calcRouteDistance(steps);
    }

    public Coord getStart() {
        return this.coords.get(0);
    }

    public Coord getEnd() {
        return this.coords.get(this.coords.size() - 1);
    }

    public int getId() {
        return this.id;
    }

    public float getDistance() {
        return this.distance;
    }

    public Coord getCoords(int index) {
        return this.coords.get(index);
    }

    public float getConsumption(VehicleDto vehicleDto) {
        return RequestsUtils.calcRouteConsumption(vehicleDto, this.steps);
    }

    public Coord getCoordAtDistance(float distance) {
        int index = 0;
        while (this.getDistanceAt(index) < distance) {
            index++;
        }
        if (index == 0) {
            return this.getCoords(0);
        }
        Coord before = getCoords(index - 1);
        Coord after = getCoords(index);
        double vector_lat = after.getLat() - before.getLat();
        double vector_lon = after.getLon() - before.getLon();
        double distance_before = this.getDistanceAt(index - 1);
        double distance_after = this.getDistanceAt(index);
        double distance_between = distance_after - distance_before;
        double ratio = (distance - distance_before) / distance_between;
        double lat = before.getLat() + vector_lat * ratio;
        double lon = before.getLon() + vector_lon * ratio;
        return new Coord(lon, lat);
    }

    private float getDistanceAt(int index) {
        return this.distanceCumulee.get(index);
    }
}
