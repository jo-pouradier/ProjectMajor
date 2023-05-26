package fr.clbd.fire.service;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {
    public VehicleDto[] getTeamVehicles() {
        return RequestsUtils.makeRequest("/vehiclebyteam/" + teamId, HttpMethod.GET, null, VehicleDto[].class);
    }

    public VehicleDto[] getAllVehicles() {
        // TODO
        return null;
    }

    public VehicleDto addVehicle(VehicleDto VehicleDto) {
        // TODO
        return null;
    }

    public boolean delAllVehicles() {
        // TODO
        return false;
    }

    public boolean delVehicle(UUID id) {
        // TODO
        return false;
    }

    public VehicleDto updateVehicle(VehicleDto VehicleDto) {
        // TODO
        return null;
    }

    public VehicleDto moveVehicle(UUID VehicleId, Coord coord) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        VehicleService VehicleService = new VehicleService();
        //VehicleDto[] test = VehicleService.getTeamVehicles(UUID.fromString("9b229cdd-42af-4fbc-845b-07c36b9fba30"));
        System.out.println(Arrays.toString(test));
    }
}
