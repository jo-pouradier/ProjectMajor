package fr.clbd.fire.model;

import com.project.model.dto.FireType;
import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleDto;
import com.project.model.dto.VehicleType;
import org.springframework.beans.BeanUtils;

public class Vehicle extends VehicleDto {
    public static final int CREW_MEMBER_START_VALUE = -1;
    private Integer id;
    private double lon;
    private double lat;
    private VehicleType type;
    private LiquidType liquidType;
    private float liquidQuantity;
    private float fuel;
    private int crewMember;
    private Integer facilityRefID;

    public Vehicle() {
        this.crewMember = -1;
        this.liquidType = LiquidType.ALL;
    }

    public Vehicle(int id, double lon, double lat, VehicleType type, LiquidType liquidType, float liquidQuantity, float fuel, int crewMember, Integer facilityRefID) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.type = type;
        this.liquidType = liquidType;
        this.liquidQuantity = liquidQuantity;
        this.fuel = fuel;
        this.crewMember = crewMember;
        this.facilityRefID = facilityRefID;
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

    public VehicleType getType() {
        return this.type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public LiquidType getLiquidType() {
        return this.liquidType;
    }

    public void setLiquidType(LiquidType liquidType) {
        this.liquidType = liquidType;
    }

    public float getLiquidQuantity() {
        return this.liquidQuantity;
    }

    public void setLiquidQuantity(float liquidQuantity) {
        this.liquidQuantity = liquidQuantity;
    }

    public float getFuel() {
        return this.fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public int getCrewMember() {
        return this.crewMember;
    }

    public void setCrewMember(int crewMember) {
        this.crewMember = crewMember;
    }

    public Integer getFacilityRefID() {
        return this.facilityRefID;
    }

    public void setFacilityRefID(Integer facilityRefID) {
        this.facilityRefID = facilityRefID;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                ", type=" + type +
                ", liquidType=" + liquidType +
                ", liquidQuantity=" + liquidQuantity +
                ", fuel=" + fuel +
                ", crewMember=" + crewMember +
                ", facilityRefID=" + facilityRefID +
                '}';
    }

    public static Vehicle fromDto(VehicleDto vehicleDto){
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDto, vehicle);
        return vehicle;
    }


    public void setOptimizedLiquid(FireType fireType) {
        LiquidType best = LiquidType.ALL;
        float bestRatio = 0;
        for(LiquidType type : LiquidType.values()){
            float ratio = type.getEfficiency(fireType.toString());
            if (ratio > bestRatio){
                best = type;
                bestRatio = ratio;
            }
        }
        this.setLiquidType(best);
    }


    public void reset() {
        this.reset(this.getType());
    }

    public void reset(VehicleType type) {
        this.setType(type);
        this.setFuel(this.getType().getFuelCapacity());
        this.setCrewMember(this.getType().getVehicleCrewCapacity());
        this.setLiquidQuantity(this.getType().getLiquidCapacity());
        this.setLiquidType(LiquidType.ALL);
    }

    public VehicleDto toDto(){
        VehicleDto vehicleDto = new VehicleDto();
        BeanUtils.copyProperties(this, vehicleDto);
        return vehicleDto;
    }

    public void refuel() {
        setFuel(getType().getFuelCapacity());
    }

    public float efficiency(FireType fireType){
        return getType().getEfficiency()*getLiquidType().getEfficiency(fireType.toString());
    }
}
