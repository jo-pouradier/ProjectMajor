package fr.clbd.fire.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/image")
public class ImageController {

    @GetMapping(value = "/svg/{imagePath}", produces = "text/plain")
    public String getTruckImage(@PathVariable String imagePath) throws IOException {
        File resource = new ClassPathResource("static/images/" + imagePath).getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }

}
