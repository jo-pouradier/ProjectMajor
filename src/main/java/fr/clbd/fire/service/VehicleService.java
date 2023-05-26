package fr.clbd.fire.service;

import com.project.model.dto.VehicleDto;
import fr.clbd.fire.model.Coord;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {
    public List<VehicleDto> getTeamVehicles(UUID teamId) {
        return RequestsUtils.makeRequest("/vehiclebyteam/" + teamId, HttpMethod.GET, null, List.class);
    }

    public VehicleDto getAllVehicles() {
        // TODO
        return null;
    }

    public VehicleDto addVehicle(UUID teamId, VehicleDto VehicleDto) {
        // TODO
        return null;
    }

    public boolean delAllVehicles(UUID teamId) {
        // TODO
        return false;
    }

    public boolean delVehicle(UUID teamId, UUID id) {
        // TODO
        return false;
    }

    public VehicleDto updateVehicle(UUID teamId, VehicleDto VehicleDto) {
        // TODO
        return null;
    }

    public VehicleDto moveVehicle(UUID teamId, UUID VehicleId, Coord coord) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        VehicleService VehicleService = new VehicleService();
        List<VehicleDto> test = VehicleService.getTeamVehicles(UUID.fromString("9b229cdd-42af-4fbc-845b-07c36b9fba30"));
        System.out.println(test);
    }
}
