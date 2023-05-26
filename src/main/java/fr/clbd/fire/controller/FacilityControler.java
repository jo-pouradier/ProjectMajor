package fr.clbd.fire.controller;

import com.project.model.dto.FacilityDto;
import fr.clbd.fire.service.FacilityService;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facility")
public class FacilityControler {

    @Autowired
    private FacilityService facilityService;

    @GetMapping(value = "/", produces = "application/json")
    public FacilityDto[] getAllFacilities() {
        return RequestsUtils.makeRequest("/facility", HttpMethod.GET, null, FacilityDto[].class);
    }

    @PostMapping(value = "/", produces = "application/json")
    public FacilityDto addFacility(FacilityDto facilityDto) {
        return RequestsUtils.makeRequest("/facility", HttpMethod.POST, facilityDto, FacilityDto.class);
    }

    @DeleteMapping(value = "/", produces = "application/json")
    public boolean delAllFacilities() {
        return Boolean.TRUE.equals(RequestsUtils.makeRequest("/facility", HttpMethod.DELETE, null, Boolean.class));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public FacilityDto getFacility(@PathVariable("id") String id) {
        return RequestsUtils.makeRequest("/facility/" + id, HttpMethod.GET, null, FacilityDto.class);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public boolean delFacility(@PathVariable("id") String id) {
        return Boolean.TRUE.equals(RequestsUtils.makeRequest("/facility/" + id, HttpMethod.DELETE, null, Boolean.class));
    }

}
