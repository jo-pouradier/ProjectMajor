package fr.clbd.fire.service;

import com.project.model.dto.FacilityDto;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FacilityService {

    public FacilityDto[] getAllFacilities() {
        return RequestsUtils.makeRequest("facility", HttpMethod.GET, null, FacilityDto[].class);
    }

    public FacilityDto addFacility(FacilityDto facilityDto) {
        //TODO ???
        return null;
    }

    public boolean delAllFacilities() {
        //TODO ???
        return false;
    }

    public boolean delFacility(UUID id) {
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
        FacilityDto[] test = facilityService.getAllFacilities();
        System.out.println(test);
    }

}
