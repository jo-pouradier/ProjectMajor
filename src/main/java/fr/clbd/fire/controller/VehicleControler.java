package fr.clbd.fire.controller;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import fr.clbd.fire.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vehicle")
public class VehicleControler {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping(value = "/getTeamVehicle", produces = "application/json")
    public VehicleDto[] getTeamVehicle() {
        return vehicleService.getTeamVehicles();
    }

    @GetMapping(value = "/getAllVehicle", produces = "application/json")
    public VehicleDto[] getAllVehicle() {
        return vehicleService.getAllVehicles();
    }
    @PostMapping(value = "/addVehicle", produces = "application/json")
    public VehicleDto addVehicle() {
        VehicleDto vehicleDto = new VehicleDto();
        return vehicleService.addVehicle(vehicleDto);
    }

    @DeleteMapping(value = "/delAllVehicle", produces = "application/json")
    public boolean delAllVehicle() {
        return vehicleService.delAllVehicles();
    }

    @DeleteMapping(value = "/delVehicle", produces = "application/json")
    public boolean delVehicle(UUID uuid){
        return vehicleService.delVehicle(uuid);
    }

    @PutMapping(value = "/updateVehicle", produces = "application/json")
    public VehicleDto updateVehicle(VehicleDto vehicleDto){
        return vehicleService.updateVehicle(vehicleDto);
    }

    @PutMapping(value = "/moveVehicle", produces = "application/json")
    public VehicleDto moveVehicle(UUID uuid, Coord coord){
        return vehicleService.moveVehicle(uuid, coord);
    }

}
