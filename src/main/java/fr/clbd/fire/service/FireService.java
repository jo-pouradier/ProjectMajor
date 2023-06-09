package fr.clbd.fire.service;

import com.project.model.dto.FireDto;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.stereotype.Service;

@Service
public class FireService {

    public FireDto[] getAllFires() {
        return RequestsUtils.getAllFires();
    }

    public FireDto getFire(int id) {
        return RequestsUtils.getFire(id);
    }

    public int getDistanceBetweenCoord(double lat1, double lon1, double lat2, double lon2) {
        return (int) RequestsUtils.getDistanceBetweenCoord(lat1, lon1, lat2, lon2);
    }
}

