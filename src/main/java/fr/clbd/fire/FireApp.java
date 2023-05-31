package fr.clbd.fire;

import fr.clbd.fire.utils.RequestsUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FireApp {
	
	public static void main(String[] args) {
		SpringApplication.run(FireApp.class,args);
		//System.out.println(RequestsUtils.getAllFires());
		RequestsUtils.tests();
	}
}
