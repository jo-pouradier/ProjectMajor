package fr.clbd.fire.service;

import com.project.model.dto.FireDto;
import com.project.model.dto.VehicleDto;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    public double calculDistance(VehicleDto vehiculeDto, FireDto fireDto) {
        double lat1 = vehiculeDto.getLat();
        double lon1 = vehiculeDto.getLon();
        double lat2 = fireDto.getLat();
        double lon2 = fireDto.getLon();
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    public float calculTimeStep2(VehicleDto vehiculeDto, double distance) {
        // mouvement linéaire
        return (float) (2 * (distance / vehiculeDto.getType().getMaxSpeed()));
    }

    public int calculTimeStep3(){
        // mouvement réelle
        return 0;
    }

    public boolean isTrajetOk(VehicleDto vehicleDto, FireDto fireDto) {
        // Pour aller retour depuis sa position actuelle
        double distance = calculDistance(vehicleDto, fireDto);
        float time = calculTimeStep2(vehicleDto, distance);
        int consoFuel = (int) (distance * vehicleDto.getType().getFuelConsumption() + 10);
        return vehicleDto.getFuel() >= consoFuel;
    }
}
