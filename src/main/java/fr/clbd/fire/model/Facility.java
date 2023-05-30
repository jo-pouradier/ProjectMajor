package fr.clbd.fire.model;

import com.project.model.dto.FacilityDto;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;

public class Facility {
    private Integer id;
    private double lon;
    private double lat;
    private String name;
    private int maxVehicleSpace;
    private int peopleCapacity;
    private Set<Integer> vehicleIdSet;
    private Set<Integer> peopleIdSet;
    private String teamUuid;

    public Facility() {
        this.vehicleIdSet = new HashSet();
        this.peopleIdSet = new HashSet();
    }

    public Facility(Integer id, double lon, double lat, String name, int maxVehicleSpace, int peopleCapacity, Set<Integer> vehicleIdSet, Set<Integer> peopleIdSet, String teamUuid) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.name = name;
        this.maxVehicleSpace = maxVehicleSpace;
        this.peopleCapacity = peopleCapacity;
        this.vehicleIdSet = vehicleIdSet;
        this.peopleIdSet = peopleIdSet;
        this.teamUuid = teamUuid;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxVehicleSpace() {
        return this.maxVehicleSpace;
    }

    public void setMaxVehicleSpace(int maxVehicleSpace) {
        this.maxVehicleSpace = maxVehicleSpace;
    }

    public int getPeopleCapacity() {
        return this.peopleCapacity;
    }

    public void setPeopleCapacity(int peopleCapacity) {
        this.peopleCapacity = peopleCapacity;
    }

    public Set<Integer> getVehicleIdSet() {
        return this.vehicleIdSet;
    }

    public void setVehicleIdSet(Set<Integer> vehicleIdSet) {
        this.vehicleIdSet = vehicleIdSet;
    }

    public Set<Integer> getPeopleIdSet() {
        return this.peopleIdSet;
    }

    public void setPeopleIdSet(Set<Integer> peopleIdSet) {
        this.peopleIdSet = peopleIdSet;
    }

    public String getTeamUuid() {
        return this.teamUuid;
    }

    public void setTeamUuid(String teamUuid) {
        this.teamUuid = teamUuid;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                ", name='" + name + '\'' +
                ", maxVehicleSpace=" + maxVehicleSpace +
                ", peopleCapacity=" + peopleCapacity +
                ", vehicleIdSet=" + vehicleIdSet +
                ", peopleIdSet=" + peopleIdSet +
                ", teamUuid='" + teamUuid + '\'' +
                '}';
    }

    public static Facility fromDto(FacilityDto facilityDto){
        Facility facility = new Facility();
        BeanUtils.copyProperties(facilityDto, facility);
        return facility;
    }
}

