package fr.clbd.fire.controller;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import com.project.model.dto.VehicleType;
import fr.clbd.fire.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @DeleteMapping(value = "/delVehicle/{id}", produces = "application/json")
    public boolean delVehicle(@PathVariable int id) {
        System.out.println("delete ok");
        return vehicleService.delVehicle(id);

    }

    @PutMapping(value = "/updateVehicle", produces = "application/json")
    public VehicleDto updateVehicle(int id, VehicleDto vehicleDto) {
        return vehicleService.updateVehicle(id, vehicleDto);
    }

    @PutMapping(value = "/moveVehicle", produces = "application/json")
    public VehicleDto moveVehicle(int id, Coord coord) {
        return vehicleService.moveVehicle(id, coord);
    }

    @GetMapping(value = "/getVehicleTypeInfo", produces = "application/json")
    public HashMap<String, Integer> getVehicleTypeInfo(@RequestParam("param") String param) {
        return vehicleService.getVehicleTypeInfo(VehicleType.valueOf(param));
    }

    @PostMapping(value ="/createVehicle", produces = "application/json")
    public VehicleDto addVehicle(@RequestBody VehicleDto vehicleDto ) {
        System.out.println("add ok");
        return vehicleService.addVehicle(vehicleDto);
    }
}
