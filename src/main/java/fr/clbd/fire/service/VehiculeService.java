package fr.clbd.fire.service;

import fr.clbd.fire.model.Coord;
import fr.clbd.fire.model.dto.VehiculeDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VehiculeService {

    public VehiculeDto getVehicule(UUID id) {
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
}
