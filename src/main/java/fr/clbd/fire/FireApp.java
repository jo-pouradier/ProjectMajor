package fr.clbd.fire;

import fr.clbd.fire.bot.BotManager;
import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.project.model.dto.VehicleDto;

import fr.clbd.fire.bot.BotManager;

@SpringBootApplication
@EnableScheduling
public class FireApp {
	
	public static void main(String[] args) {
		new BotManager();
		SpringApplication.run(FireApp.class, args);
		//System.out.println(RequestsUtils.getAllFires());
//		VehicleDto vehicleDto = RequestsUtils.getVehicle(3799);
//		BotManager.getInstance().createBot(vehicleDto);
//		VehicleDto vehicleDto2 = RequestsUtils.getVehicle(4223);
//		BotManager.getInstance().createBot(vehicleDto2);
//		VehicleDto fire_engine1 = RequestsUtils.getVehicle(4369);
//		BotManager.getInstance().createBot(fire_engine1);
//		VehicleDto fire_engine2 = RequestsUtils.getVehicle(4533);
//		BotManager.getInstance().createBot(fire_engine2);
//		RequestsUtils.testBots();
	}
}
