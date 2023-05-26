package fr.clbd.fire.controller;

import com.project.model.dto.FacilityDto;
import fr.clbd.fire.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/facility")
public class FacilityControler {

    @Autowired
    private FacilityService facilityService;

    @GetMapping(value = "/", produces = "application/json")
    public FacilityDto[] getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @PostMapping(value = "/", produces = "application/json")
    public FacilityDto addFacility(@RequestBody FacilityDto facilityDto) {
        return facilityService.addFacility(facilityDto);
    }

    @DeleteMapping(value = "/", produces = "application/json")
    public boolean delAllFacilities() {
        return facilityService.delAllFacilities();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public FacilityDto getFacility(@PathVariable("id") String id) {
        return facilityService.getFacility(UUID.fromString(id));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public boolean delFacility(@PathVariable("id") String id) {
        return facilityService.delFacility(UUID.fromString(id));
    }

}
