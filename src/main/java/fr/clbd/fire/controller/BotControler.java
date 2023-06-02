package fr.clbd.fire.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.clbd.fire.bot.Bot;
import fr.clbd.fire.bot.BotManager;

@RestController
@RequestMapping("/bot")
public class BotControler {
    



    @GetMapping("/getBots")
    public Collection<Bot> getBots() {
        return BotManager.getInstance().getBots().values();
    }

}
