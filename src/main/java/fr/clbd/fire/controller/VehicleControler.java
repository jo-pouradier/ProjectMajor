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
    @GetMapping(value = "/getVehicle", produces = "application/json")
    public VehicleDto getVehicle(int Id) {
        return vehicleService.getVehicle(Id);
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

    @DeleteMapping(value = "/delVehicle/{teamId}/{id}", produces = "application/json")
    public boolean delVehicle(@PathVariable int id, @PathVariable int teamId) {
        return vehicleService.delVehicle(id, teamId);
    }

    @PutMapping(value = "/updateVehicle", produces = "application/json")
    public VehicleDto updateVehicle(int id, VehicleDto vehicleDto) {
        return vehicleService.updateVehicle(id, vehicleDto);
    }

    @PutMapping(value = "/moveVehicle", produces = "application/json")
    public VehicleDto moveVehicle(int id, Coord coord) {
        return vehicleService.moveVehicle(id, coord);
    }

}
