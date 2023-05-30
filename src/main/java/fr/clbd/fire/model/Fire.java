package fr.clbd.fire.model;

import com.project.model.dto.FireDto;
import org.springframework.beans.BeanUtils;

public class Fire {

    private Integer id;
    private String type;
    private float intensity;
    private float range;
    private double lon;
    private double lat;

    public Fire() {
    }

    public Fire(Integer id, String type, float intensity, float range, double lon, double lat) {
        this.id = id;
        this.type = type;
        this.intensity = intensity;
        this.range = range;
        this.lon = lon;
        this.lat = lat;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getIntensity() {
        return this.intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float getRange() {
        return this.range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public static Fire fromDto(FireDto fakeFireDto) {
        Fire fire = new Fire();
        BeanUtils.copyProperties(fakeFireDto, fire);
        return fire;
    }
}
