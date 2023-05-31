package fr.clbd.fire.bot;

import com.project.model.dto.VehicleDto;
import fr.clbd.fire.model.Vehicle;
import lombok.Getter;

public class BotDto {

    @Getter
    private int id;
    @Getter
    private VehicleDto vehicleDto;

    public BotDto(int id, VehicleDto vehicleDto) {
        this.id = id;
        this.vehicleDto = vehicleDto;
    }

    public BotDto(VehicleDto vehicleDto) {
        this.vehicleDto = vehicleDto;
    }

    public BotDto() {
        this.id = -1;
        this.vehicleDto = new VehicleDto();
    }

    public VehicleDto getVehicleDto() {
        return vehicleDto;
    }

    public Vehicle getVehicle() {
        return Vehicle.fromDto(vehicleDto);
    }
}
