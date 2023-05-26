package fr.clbd.fire.service;

import fr.clbd.fire.model.dto.FacilityDto;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import fr.clbd.fire.utils.RequestsUtils;

import java.util.List;
import java.util.UUID;

@Service
public class FacilityService {

    public List<FacilityDto> getAllFacilities() {
        return RequestsUtils.makeRequest("facility", HttpMethod.GET, null,List.class);
    }

    public FacilityDto addFacility(FacilityDto facilityDto) {
        //TODO ???
        return null;
    }

    public boolean delAllFacilities() {
        //TODO ???
        return false;
    }

    public FacilityDto getFacility(UUID id) {
        //TODO ???
        return null;
    }

    // TODO get facility OBJECT

    public static void main(String[] args) {
        FacilityService facilityService = new FacilityService();
        List<FacilityDto> test = facilityService.getAllFacilities();
    }

}
