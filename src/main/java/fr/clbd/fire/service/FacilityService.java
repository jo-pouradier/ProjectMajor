package fr.clbd.fire.service;

import com.project.model.dto.FacilityDto;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {

    public FacilityDto[] getAllFacilities() {
        return RequestsUtils.getFacilities();
    }

    public FacilityDto addFacility(FacilityDto facilityDto) {
        return RequestsUtils.addFacility(facilityDto);
    }

    public boolean delAllFacilities() {
        return RequestsUtils.delAllFacilities();
    }

    public boolean delFacility(int id) {
        return Boolean.TRUE.equals(RequestsUtils.delFacility(id));
    }

    public FacilityDto getFacility(int id) {
        return RequestsUtils.getFacility(id);
    }

    public static void main(String[] args) {
        FacilityService facilityService = new FacilityService();
        FacilityDto[] test = facilityService.getAllFacilities();
        System.out.println(test);
    }

}
