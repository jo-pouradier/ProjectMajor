package fr.clbd.fire.service;

import com.project.model.dto.VehicleDto;
import fr.clbd.fire.model.Coord;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehiculeService {
    public List<VehicleDto> getTeamVehicules(UUID teamId) {
        return RequestsUtils.makeRequest("/vehiclebyteam/" + teamId, HttpMethod.GET, null,List.class);
    }

    public VehicleDto getAllVehicules() {
        // TODO
        return null;
    }

    public VehicleDto addVehicule(UUID teamId, VehicleDto vehiculeDto) {
        // TODO
        return null;
    }

    public boolean delAllVehicules(UUID teamId) {
        // TODO
        return false;
    }

    public boolean delVehicule(UUID teamId, UUID id) {
        // TODO
        return false;
    }

    public VehicleDto updateVehicule(UUID teamId, VehicleDto vehiculeDto) {
        // TODO
        return null;
    }

    public VehicleDto moveVehicule(UUID teamId, UUID vehiculeId, Coord coord) {
        // TODO
        return null;
    }
    public static void main(String[] args) {
        VehiculeService vehiculeService = new VehiculeService();
        List<VehicleDto> test = vehiculeService.getTeamVehicules(UUID.fromString("9b229cdd-42af-4fbc-845b-07c36b9fba30"));
        System.out.println(test);
    }
}
