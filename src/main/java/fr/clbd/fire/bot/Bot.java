package fr.clbd.fire.bot;

import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.model.dto.VehicleDto;
import fr.clbd.fire.model.Vehicle;
import fr.clbd.fire.service.FireService;
import fr.clbd.fire.utils.RequestsUtils;
import fr.clbd.fire.utils.Trajet;
import lombok.Getter;

public class Bot {

    private static final float simulationSpeed = 8.0f;
    private static final FireService fireService = new FireService();

    @Getter
    private Vehicle vehicle;

    @Getter
    private int id;

    @Getter
    private Trajet trajet;

    private double trajetDistance = 0.0f;
    private double trajetPas = 0.0f;

    public Bot(int id, Vehicle vehicle) {
        this.id = id;
        this.vehicle = vehicle;
    }

    public boolean run() {
        if (this.trajet == null) {
            return false;
        }
        if (this.trajetDistance >= this.trajet.getDistance()) {
            Coord newCoord = this.trajet.getEnd();
            this.vehicle.setLat(newCoord.getLat());
            this.vehicle.setLon(newCoord.getLon());
            this.moveVehicle();
            this.resetTrajet();
            this.stop();
            return false;
        }
        trajetDistance += this.trajetPas;
        Coord newCoord = this.trajet.getCoordAtDistance((float)this.trajetDistance);
        this.vehicle.setLat(newCoord.getLat());
        this.vehicle.setLon(newCoord.getLon());
        this.moveVehicle();
        return true;
    }

    public void moveVehicle() {
        VehicleDto vehicleDto = RequestsUtils.moveVehicle(this.vehicle.getId(), this.vehicle.toDto());
        this.vehicle = Vehicle.fromDto(vehicleDto);
    }

    public void stop() {
        System.out.println("Bot " + this.id + "is stopped");
        this.trajet = null;
    }

    public BotDto toDto() {
        return new BotDto(vehicle.toDto());
    }

    public static Bot fromDto(BotDto botDto) {
        return new Bot(botDto.getId(), Vehicle.fromDto(botDto.getVehicleDto()));
    }

    public Trajet createTrajetToFire(){
        FireDto fire = fireService.getAllFires()[0];
         return new Trajet(fire.getId(),new Coord(this.getVehicle().getLon(), this.getVehicle().getLat()),new Coord(fire.getLon(), fire.getLat()));
    }


    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        this.resetTrajet();
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