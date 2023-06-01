package fr.clbd.fire.bot;

import com.project.model.dto.VehicleDto;
import fr.clbd.fire.model.Vehicle;
import fr.clbd.fire.utils.RequestsUtils;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class BotManager {

    public static final long updateSpeed = 1000; //ms
    @Getter
    private static BotManager instance;
    private HashMap<Integer, Bot> bots;
    private int id = 0;

    public BotManager() {
        RequestsUtils.info("BotManager created");
        this.bots = new HashMap<>();
        instance = this;
    }

    public HashMap<Integer, Bot> getBots() {
        return this.bots;
    }

    public Bot getBot(int id) {
        return this.bots.get(id);
    }

    public Bot createBot(VehicleDto vehicleDto) {
        Bot bot = new Bot(id, Vehicle.fromDto(vehicleDto));
        this.bots.put(id, bot);
        RequestsUtils.info(this.bots.size());
        id++;
        return bot;
    }

    public void removeBot(Bot bot) {
        this.bots.remove(bot.getId());
    }


    @Scheduled(fixedRate = updateSpeed)
    public void process() {
        //RequestsUtils.info("BotManager process: " + this.bots.size() + " bots");
        for (Bot bot : this.bots.values()) {
            RequestsUtils.info("Bot " + bot.getId());
            if (!bot.run()) {

            }
        }
    }

}
