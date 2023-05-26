package fr.clbd.fire.service;

import fr.clbd.fire.model.VehiculeType;
import fr.clbd.fire.model.dto.FireDto;
import fr.clbd.fire.model.dto.VehiculeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {

    public double calculDistance(VehiculeDto vehiculeDto, FireDto fireDto){
        double lat1 = vehiculeDto.getLat();
        double lon1 = vehiculeDto.getLon();
        double lat2 = fireDto.getLat();
        double lon2 = fireDto.getLon();
        return Math.sqrt(Math.pow(lat2-lat1,2)+Math.pow(lon2-lon1,2));
    }

    public float calculTimeStep2(VehiculeDto vehiculeDto, double distance){
        // mouvement linéaire
        return (float) (2*(distance/vehiculeDto.getType().getMaxSpeed()));
    }

    public int calculTimeStep3(){
        // mouvement réelle
        return 0;
    }

    public boolean isTrajetOk(VehiculeDto vehiculeDto, FireDto fireDto){
        // Pour aller retour depuis sa position actuelle
        double distance = calculDistance(vehiculeDto, fireDto);
        float time = calculTimeStep2(vehiculeDto, distance);
        int consoFuel = (int) (distance*vehiculeDto.getType().getFuelConsumption()+10);
        return vehiculeDto.getFuel() >= consoFuel;
    }
}
