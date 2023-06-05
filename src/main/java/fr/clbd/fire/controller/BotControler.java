package fr.clbd.fire.controller;

import java.util.Collection;
import java.util.List;

import com.project.model.dto.VehicleDto;
import fr.clbd.fire.service.VehicleService;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.clbd.fire.bot.Bot;
import fr.clbd.fire.bot.BotManager;

@RestController
@RequestMapping("/bot")
public class BotControler {

    @PutMapping("/createBot/{idVehicle}")
    public ResponseEntity<?> createBot(@PathVariable(value = "idVehicle") int idVehicle) {
        VehicleDto vehicleDto = RequestsUtils.getVehicle(idVehicle);
        Bot bot = BotManager.getInstance().createBot(vehicleDto);
        return ResponseEntity.ok().body(bot.getId());
    }

    @DeleteMapping("/deleteBot/{idBot}")
    public ResponseEntity<?> deleteBot(@PathVariable(value = "idBot") int idBot) {
        Bot bot = BotManager.getInstance().getBot(idBot);
        if (bot == null) {
            return ResponseEntity.notFound().build();
        }
        BotManager.getInstance().removeBot(bot);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getBots")
    public Collection<Bot> getBots() {
        return BotManager.getInstance().getBots().values();
    }

}
