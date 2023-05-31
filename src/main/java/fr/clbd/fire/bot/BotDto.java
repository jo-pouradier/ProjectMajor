package fr.clbd.fire.bot;

import com.project.model.dto.VehicleDto;

public class BotDto {


    private VehicleDto vehicleDto;

    public BotDto(VehicleDto vehicleDto) {
        this.vehicleDto = vehicleDto;
    }

    public BotDto() {
        this.vehicleDto = new VehicleDto();
    }

    public VehicleDto getVehicleDto() {
        return vehicleDto;
    }

}
