package fr.clbd.fire.controller;

import fr.clbd.fire.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facility")
public class FacilityControler {

    @Autowired
    private FacilityService facilityService;

    @GetMapping(value = "/", produces = "application/json")
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }


}
