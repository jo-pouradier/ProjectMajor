package fr.clbd.fire.utils;

import com.project.model.dto.*;
import com.project.tools.GisTools;
import fr.clbd.fire.model.Facility;
import fr.clbd.fire.model.Fire;
import fr.clbd.fire.model.Vehicle;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class RequestsUtils {
    final static String baseUrl = "http://vps.cpe-sn.fr:8081/";

    private static final String uuid = "9b229cdd-42af-4fbc-845b-07c36b9fba30";

    private static final String teamName = "Fa Triangle (v4)";
    /**
    * @param <T> Response type
    * @param <U> body type as dtos
    */

    public static <T, U> T makeRequest(String url, Class<T> responseType) {
        return RequestsUtils.makeRequest(url, HttpMethod.GET, null,responseType);
    }
    public static <T, U> T makeRequest(String url, HttpMethod method, U body, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        try {

            HttpEntity<U> request = new HttpEntity<U>(body);
            ResponseEntity<T> response = restTemplate.exchange(baseUrl + url, method, request, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                T responseBody = response.getBody();
                System.out.println(responseBody);
                return responseBody;
            } else {
                System.out.println("La requête a échoué avec le code : " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            System.out.println("Erreur dans la requête : " + e.getMessage());
        }
        return null;
    }

    // ------ FIRE --------
    public static FireDto[] getAllFires() {
        return makeRequest("/fires", FireDto[].class);
    }

    public static FireDto getFire(int id) {
        return makeRequest("/fire/" + id, FireDto.class);
    }

    public static int getDistanceBetweenCoord(double lat1, double lon1, double lat2, double lon2) {
        return (int)(GeoUtils.getDistance(new Coord(lon1, lat1), new Coord(lon2, lat2))*1000);
//        return GisTools.computeDistance2(new Coord(lon1, lat1), new Coord(lon2, lat2));
    }

    // ------ FACILITY --------
    public static FacilityDto[] getFacilities() {
        return makeRequest("/facility", FacilityDto[].class);
    }

    public static FacilityDto getFacility(int id) {
        return makeRequest("/facility/" + id, FacilityDto.class);
    }

    public static FacilityDto addFacility(FacilityDto facilityDto) {
        return makeRequest("/facility/" + uuid, HttpMethod.POST, facilityDto, FacilityDto.class);
    }

    public static boolean delAllFacilities() {
        return Boolean.TRUE.equals(makeRequest("/facility", HttpMethod.DELETE, null, Boolean.class));
    }

    public static boolean delFacility(int id) {
        return Boolean.TRUE.equals(makeRequest("/facility/" + id, HttpMethod.DELETE, null, Boolean.class));
    }


    // ------ VEHICLE --------
    public static VehicleDto[] getTeamVehicles() {
        return makeRequest("/vehiclebyteam/" + uuid, VehicleDto[].class);
    }
    public static VehicleDto getVehicle(int id) {
        return makeRequest("/vehicle/" + id,VehicleDto.class);
    }

    public static VehicleDto[] getAllVehicles() {
        return makeRequest("/vehicles", VehicleDto[].class);
    }

    public static boolean delAllVehicles() {
        return Boolean.TRUE.equals(makeRequest("/vehicle", HttpMethod.DELETE, null, Boolean.class));
    }

    public static boolean delVehicle(int id) {
        return Boolean.TRUE.equals(makeRequest("/vehicle/" + id, HttpMethod.DELETE, null, Boolean.class));
    }

    public static Coord getCoord(double lon, double lat) {
        return new Coord(lon, lat);
    }

    public static FireDto generateFakeFire(Coord coord) {
        FireDto fireDto = new FireDto();
        fireDto.setLat(coord.getLat());
        fireDto.setLon(coord.getLon());
        fireDto.setIntensity(100);
        fireDto.setRange(50);
        return fireDto;
    }

    public static VehicleDto addVehicle(VehicleDto vehicleDto) {
        return makeRequest("/vehicle/" + uuid, HttpMethod.POST, vehicleDto, VehicleDto.class);
    }

    public static VehicleDto moveVehicle(int id, Coord coord) {
        return makeRequest("/vehicle/move/" + uuid + "/" + id, HttpMethod.PUT, coord, VehicleDto.class);
    }
    public static VehicleDto moveVehicle(int id, VehicleDto vehicleDto) {
        return moveVehicle(id, new Coord(vehicleDto.getLon(), vehicleDto.getLat()));
    }
    public static VehicleDto updateVehicle(int id, VehicleDto vehicleDto) {
        return makeRequest("/vehicle/" + uuid + "/" + id, HttpMethod.PUT, vehicleDto, VehicleDto.class);
    }

    public static boolean isAtFire(VehicleDto vehicleDto, FireDto fireDto) {
        return GisTools.computeDistance2(new Coord(vehicleDto.getLon(), vehicleDto.getLat()), new Coord(fireDto.getLat(), fireDto.getLon())) <= fireDto.getRange();
    }

    public static void info(Object message){
        System.out.println(message);
    }
    public static void main(String[] args) {
        FacilityDto[] facilities = getFacilities();
        FacilityDto ownedFacilityDto = Arrays.stream(facilities).filter(facilityDto -> {
           return facilityDto.getName().equals(RequestsUtils.teamName);
        }).findFirst().get();
        Facility ownedFacility = Facility.fromDto(ownedFacilityDto);
        System.out.println("Owned facility : " + ownedFacility);
        FacilityDto otherFacilityDto = Arrays.stream(facilities).filter(facilityDto -> {
            return !facilityDto.getName().equals(RequestsUtils.teamName);
        }).findFirst().get();
        Facility otherFacility = Facility.fromDto(otherFacilityDto);
        FacilityDto cibledFacility = getFacility(40);
        System.out.println("Other facility : " + otherFacility);
        FireDto[] fires = getAllFires();
        RequestsUtils.info(fires.length+" fires");
        FireDto fakeFireDto = generateFakeFire(getCoord(otherFacility.getLon(), otherFacility.getLat()));
        Fire fakeFire = Fire.fromDto(fakeFireDto);
        for(FireDto fire : fires) {

        }
        VehicleDto truckDto = getVehicle(3799);
        Vehicle truck = Vehicle.fromDto(truckDto);
        RequestsUtils.info("truck 1: " + truck);
        moveToCoordInThread(new Coord(fakeFire.getLon(),fakeFire.getLat()), truckDto);
        truckDto = getVehicle(3799);
        moveToCoordInThread(new Coord(ownedFacility.getLon(),ownedFacility.getLat()), truckDto);
        }
        public static void waitFireOff(int fireId){
            FireDto fireDto = getFire(fireId);
            while(fireDto.getIntensity() > 0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fireDto = getFire(fireId);
            }
        }
        public static void moveToCoordInThread(Coord fire, VehicleDto vehicleDto){
            // Coefficient accelerateur de mouvement
            float speed_coef = 10;
            int start_distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
            // Ligne droite
            double vector_x = fire.getLat()-vehicleDto.getLat();
            double vector_y = fire.getLon()-vehicleDto.getLon();
            // Normalise
//            vector_x = vector_x / Math.sqrt(vector_x * vector_x + vector_y * vector_y);
//            vector_y = vector_y / Math.sqrt(vector_x * vector_x + vector_y * vector_y);
            // time in seconds
            double time = 3600 * ((start_distance/1000.0) / (vehicleDto.getType().getMaxSpeed()) )/ speed_coef;
            double step_x = vector_x / time;
            double step_y = vector_y / time;
            info("step_x : " + step_x);
            info("step_y : " + step_y);
            double step_distance = getDistanceBetweenCoord(step_x,step_y, 0, 0);
            Thread thread = new Thread(() -> {
                try {
                    double new_lat = vehicleDto.getLat()+step_x;
                    double new_lon = vehicleDto.getLon()+step_x;
                    vehicleDto.setLat(new_lat);
                    vehicleDto.setLon(new_lon);
                    int distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
                    moveVehicle(vehicleDto.getId(), vehicleDto);
                    info("distance : " + distance);
                    while(distance > step_distance){
                        new_lat = vehicleDto.getLat()+step_x;
                        new_lon = vehicleDto.getLon()+step_y;
                        vehicleDto.setLat(new_lat);
                        vehicleDto.setLon(new_lon);
                        distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
                        if (distance >= step_distance){
                            moveVehicle(vehicleDto.getId(), vehicleDto);
                        }
                        info("distance : " + distance);
                        Thread.sleep(1000);
                    }
                    new_lat = fire.getLat();
                    new_lon = fire.getLon();
                    vehicleDto.setLat(new_lat);
                    vehicleDto.setLon(new_lon);
                    distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
                    moveVehicle(vehicleDto.getId(), vehicleDto);
                    info("distance : " + distance);
                    info("Arrived at fire");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

}
