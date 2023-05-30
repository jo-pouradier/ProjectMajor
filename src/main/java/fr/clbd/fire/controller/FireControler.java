package fr.clbd.fire.controller;

import com.project.model.dto.FireDto;
import fr.clbd.fire.service.FireService;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/fire")
public class FireControler {

    @Autowired
    private FireService fireService;

    @GetMapping(value = "/", produces = "application/json")
    public FireDto[] getAllFires() {
        return fireService.getAllFires();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public FireDto getFire(@PathVariable String id) {
        return fireService.getFire((Integer.valueOf(id)));
    }

    @GetMapping(value = "/distance", produces = "application/json")
    public Integer getFiresByDistance(@RequestBody Double[] coordinates) {
        return fireService.getDistanceBetweenCoord(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }
}
