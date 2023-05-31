package fr.clbd.fire.bot;

import fr.clbd.fire.model.Vehicle;

public class Bot {


    Vehicle vehicle;

    public Bot(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void run() {
        System.out.println("Bot is running");
    }

    public void stop() {
        System.out.println("Bot is stopped");
    }

    public BotDto toDto() {
        return new BotDto(vehicle.toDto());
    }

}