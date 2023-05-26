package fr.clbd.fire.utils;

import com.project.model.dto.*;
import com.project.tools.GisTools;
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
        return makeRequest("/distance?latCoord1=" + lat1 + "&lonCoord1=" + lon1 + "&latCoord2=" + lat2 + "&lonCoord2=" + lon2, Integer.class);
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
        return makeRequest("/vehicle", VehicleDto[].class);
    }

    public static boolean delAllVehicles() {
        return Boolean.TRUE.equals(makeRequest("/vehicle", HttpMethod.DELETE, null, Boolean.class));
    }

    public static boolean delVehicle(int id) {
        return Boolean.TRUE.equals(makeRequest("/vehicle/" + id, HttpMethod.DELETE, null, Boolean.class));
    }

    public static VehicleDto addVehicle(VehicleDto vehicleDto) {
        return makeRequest("/vehicle/" + uuid, HttpMethod.POST, vehicleDto, VehicleDto.class);
    }

    public static VehicleDto moveVehicle(int id, Coord coord) {
        return makeRequest("/vehicle/" + uuid + "/" + id, HttpMethod.PUT, coord, VehicleDto.class);
    }
    public static VehicleDto updateVehicle(int id, VehicleDto vehicleDto) {
        return makeRequest("/vehicle/" + uuid + "/" + id, HttpMethod.PUT, vehicleDto, VehicleDto.class);
    }

    public static void main(String[] args) {
        FacilityDto[] facilities = getFacilities();
        FacilityDto ownedFacility = Arrays.stream(facilities).filter(facilityDto -> {
           return facilityDto.getName().equals(RequestsUtils.teamName);
        }).findFirst().get();

        System.out.println("Notre facility:" + ownedFacility.getLat() + ":"+ ownedFacility.getLon());


        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setCrewMember(6);
        vehicleDto.setLat(ownedFacility.getLat());
        vehicleDto.setLon(ownedFacility.getLon());
        vehicleDto.setFacilityRefID(ownedFacility.getId());
        vehicleDto.setType(VehicleType.TURNTABLE_LADDER_TRUCK);
        vehicleDto.setFuel(VehicleType.TURNTABLE_LADDER_TRUCK.getFuelCapacity());
        vehicleDto.setLiquidQuantity(VehicleType.TURNTABLE_LADDER_TRUCK.getLiquidCapacity());
        VehicleDto createVehicle = addVehicle(vehicleDto);
        System.out.println(createVehicle);
        System.out.println(Arrays.asList(getAllFires()));
    }
}
