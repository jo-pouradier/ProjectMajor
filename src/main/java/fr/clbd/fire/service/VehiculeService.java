package fr.clbd.fire.service;

import fr.clbd.fire.model.Coord;
import fr.clbd.fire.model.dto.FacilityDto;
import fr.clbd.fire.model.dto.VehiculeDto;
import fr.clbd.fire.utils.requests;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehiculeService {
    public List<VehiculeDto> getTeamVehicules(UUID teamId) {
        return requests.makeRequest("/vehiclebyteam/" + teamId, HttpMethod.GET, null,List.class);
    }

    public VehiculeDto getAllVehicules() {
        // TODO
        return null;
    }

    public VehiculeDto addVehicule(UUID teamId, VehiculeDto vehiculeDto) {
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

    public VehiculeDto updateVehicule(UUID teamId, VehiculeDto vehiculeDto) {
        // TODO
        return null;
    }

    public VehiculeDto moveVehicule(UUID teamId, UUID vehiculeId, Coord coord) {
        // TODO
        return null;
    }
    public static void main(String[] args) {
        VehiculeService vehiculeService = new VehiculeService();
        List<VehiculeDto> test = vehiculeService.getTeamVehicules(UUID.fromString("9b229cdd-42af-4fbc-845b-07c36b9fba30"));
        System.out.println(test);
    }
}
