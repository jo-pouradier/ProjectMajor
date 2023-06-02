package fr.clbd.fire.bot;

import java.util.Arrays;

import com.project.model.dto.Coord;
import com.project.model.dto.FacilityDto;
import com.project.model.dto.FireDto;
import com.project.model.dto.FireType;
import com.project.model.dto.VehicleDto;
import fr.clbd.fire.model.Vehicle;
import fr.clbd.fire.service.FireService;
import fr.clbd.fire.utils.RequestsUtils;
import fr.clbd.fire.utils.Trajet;
import lombok.Getter;

public class Bot {

    private static final float simulationSpeed = 10.0f;

    @Getter
    private Vehicle vehicle;

    @Getter
    private int id;

    @Getter
    private Trajet trajet;

    @Getter
    private FireDto fireDto;

    private double trajetDistance = 0.0f;
    private double trajetPas = 0.0f;

    public Bot(int id, Vehicle vehicle) {
        this.id = id;
        this.vehicle = vehicle;
    }

    public boolean run() {
        if(this.trajetDistance > 10000){
            return false;
        }
        this.vehicle = Vehicle.fromDto(RequestsUtils.getVehicle(this.vehicle.getId()));
        if (fireDto != null) {
            updateFireDto();
        }
        if (this.trajet == null) {
            return false;
        }
        if (fireDto != null){
            RequestsUtils.info("Bot "+this.getId()+" move to fire " + fireDto.getId() + " " + (trajet.getDistance()-this.trajetDistance)+"m");
        }
        if (this.trajetDistance >= this.trajet.getDistance()) {
            Coord newCoord = this.trajet.getEnd();
            this.vehicle.setLat(newCoord.getLat());
            this.vehicle.setLon(newCoord.getLon());
            this.moveVehicle(newCoord);
            this.resetTrajet();
            this.stop();
            return false;
        }
        trajetDistance += this.trajetPas;
        Coord newCoord = this.trajet.getCoordAtDistance((float)this.trajetDistance);
        this.moveVehicle(newCoord);
        return true;
    }

    public void updateFireDto() {
        if (this.fireDto != null) {
            this.fireDto = RequestsUtils.getFire(this.fireDto.getId());
        }
    }

    public void moveVehicle(Coord coord) {
        VehicleDto vehicleDto2 = RequestsUtils.getVehicle(this.vehicle.getId());
        RequestsUtils.moveVehicle(this.vehicle.getId(), coord);
        // this.vehicle = Vehicle.fromDto(vehicleDto);
    }

    public void stop() {
        System.out.println("Bot " + this.id + "is stopped");
        this.trajet = null;
        this.trajetDistance = 0.0f;
        if (this.isAtFacility()) {
            this.vehicle = Vehicle.fromDto(RequestsUtils.getVehicle(this.vehicle.getId()));
            this.vehicle.reset();
            if (fireDto != null) {
                this.vehicle.setOptimizedLiquid(FireType.valueOf(this.fireDto.getType()));
            }
            RequestsUtils.updateVehicle(this.vehicle.getId(), this.vehicle.toDto());
        }
    }

    public boolean isAtFire(){
        FireDto[] fireDtos = RequestsUtils.getAllFires();
        for (FireDto fireDto : fireDtos) {
            if (fireDto.getLat() == vehicle.getLat() && fireDto.getLon() == vehicle.getLon()) {
                return true;
            }
        }
        return false;
    }

    public FireDto getClosestFire() {
        FireDto[] fireDtos = RequestsUtils.getAllFires();
        // exclude fires electric
        fireDtos = Arrays.asList(fireDtos).stream().filter(fireDto -> !fireDto.getType().equals(FireType.E_Electric.toString())).toArray(FireDto[]::new);
        if (fireDtos.length == 0) {
            return null;
        }
        double closestDistance = Double.MAX_VALUE;
        FireDto closestFire = null;
        for (FireDto fireDto : fireDtos) {
            double distance = Math.sqrt(Math.pow(fireDto.getLat() - vehicle.getLat(), 2) + Math.pow(fireDto.getLon() - vehicle.getLon(), 2));
            if (distance < closestDistance) {
                closestDistance = distance;
                closestFire = fireDto;
            }
        }
        return closestFire;
    }

    public BotDto toDto() {
        return new BotDto(vehicle.toDto());
    }

    public static Bot fromDto(BotDto botDto) {
        return new Bot(botDto.getId(), Vehicle.fromDto(botDto.getVehicleDto()));
    }

    public void processFire(FireDto fire){
        this.fireDto = fire;
        if (this.fireDto.getLat() == this.vehicle.getLat() && this.fireDto.getLon() == this.vehicle.getLon()){
            // this.stop();
            return;
        }
        this.vehicle = Vehicle.fromDto(RequestsUtils.getVehicle(this.vehicle.getId()));
        Trajet trajet = new Trajet(0,new Coord(vehicle.getLon(), vehicle.getLat()), new Coord(fire.getLon(), fire.getLat()));
        FacilityDto facilityDto = RequestsUtils.getFacility(vehicle.getFacilityRefID());
        Trajet retour = new Trajet(0,new Coord(fire.getLon(), fire.getLat()), new Coord(facilityDto.getLon(),facilityDto.getLat()));
        // if (trajet.getConsumption(vehicle.toDto()) + retour.getConsumption(vehicle.toDto()) > vehicle.getFuel()){
        //     trajet = new Trajet(0,new Coord(vehicle.getLon(), vehicle.getLat()), new Coord(facilityDto.getLon(),facilityDto.getLat()));
        // }
        if (isAtFacility()){
            this.vehicle = Vehicle.fromDto(RequestsUtils.getVehicle(this.vehicle.getId()));
            this.vehicle.reset();
            this.vehicle.setOptimizedLiquid(FireType.valueOf(fire.getType()));
            RequestsUtils.updateVehicle(this.vehicle.getId(), this.vehicle.toDto());
        }else{
            Trajet home = new Trajet(0,new Coord(vehicle.getLon(), vehicle.getLat()), new Coord(facilityDto.getLon(),facilityDto.getLat()));
            if (vehicle.getLiquidQuantity() < vehicle.getLiquidQuantity()*0.1 || !this.vehicle.getLiquidType().equals(this.vehicle.getOptimizedLiquid(FireType.valueOf(fire.getType())))){
                trajet = home;
            }
        }
        this.setTrajet(trajet);
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        this.resetTrajet();
    }

    public boolean isAtFacility() {
        FacilityDto facilityDto = RequestsUtils.getFacility(vehicle.getFacilityRefID());
        if (facilityDto == null) {
            return false;
        }
        return facilityDto.getLat() == vehicle.getLat() && facilityDto.getLon() == vehicle.getLon();
    }

    private void resetTrajet() {
        this.trajetDistance = 0;
        float speed = vehicle.getType().getMaxSpeed(); // km/h
        speed = speed / 3.6f; // m/s
        speed = speed * simulationSpeed; // m/s
        this.trajetPas = (BotManager.updateSpeed / 1000.0) * speed;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "vehicle=" + vehicle +
                ", id=" + id +
                '}';
    }
}