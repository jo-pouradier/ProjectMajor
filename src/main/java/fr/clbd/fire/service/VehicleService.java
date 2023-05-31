package fr.clbd.fire.service;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    public VehicleDto[] getTeamVehicles() {
        return RequestsUtils.getTeamVehicles();
    }

    public VehicleDto getVehicle(int id) {
        return RequestsUtils.getVehicle(id);
    }

    public VehicleDto[] getAllVehicles() {
        return RequestsUtils.getAllVehicles();
    }

    public VehicleDto addVehicle(VehicleDto VehicleDto) {
        return RequestsUtils.addVehicle(VehicleDto);
    }

    public boolean delAllVehicles() {
        return RequestsUtils.delAllVehicles();
    }

    public boolean delVehicle(int id, int teamId) {
        return RequestsUtils.delVehicle(id, teamId);
    }

    public VehicleDto updateVehicle(int id, VehicleDto VehicleDto) {
        return RequestsUtils.updateVehicle(id, VehicleDto);
    }

    public VehicleDto moveVehicle(int VehicleId, Coord coord) {
        return RequestsUtils.moveVehicle(VehicleId, coord);
    }

    public static void main(String[] args) {
        VehicleService VehicleService = new VehicleService();
        //VehicleDto[] test = VehicleService.getTeamVehicles(UUID.fromString("9b229cdd-42af-4fbc-845b-07c36b9fba30"));
        //System.out.println(Arrays.toString(test));
    }
}
