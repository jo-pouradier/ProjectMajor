package fr.clbd.fire.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.project.model.dto.*;
import com.project.tools.GisTools;
import fr.clbd.fire.bot.Bot;
import fr.clbd.fire.bot.BotManager;
import fr.clbd.fire.model.Facility;
import fr.clbd.fire.model.Fire;
import fr.clbd.fire.model.Vehicle;
import fr.clbd.fire.utils.google.LatLongDto;
import fr.clbd.fire.utils.google.StepDto;
import fr.clbd.fire.utils.google.TrajetDto;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1


public class RequestsUtils {
    private final static String baseUrl = "http://vps.cpe-sn.fr:8081/";

    private final static String uuid;

    private static final String teamName = "Fa Triangle (v4)";

    private final static String google_route_api_key;

    static {
        try {
            uuid = loadToken();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            google_route_api_key = loadGoogleRouteApiKey();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String loadToken() throws FileNotFoundException {
        File file = new File("fire_token");
        FileReader fileReader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        try {
            fileReader.read(chars);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String content = new String(chars);
        return content;
    }

    private static String loadGoogleRouteApiKey() throws FileNotFoundException {
        File file = new File("google_route_api_key");
        FileReader fileReader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        try {
            fileReader.read(chars);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String content = new String(chars);
        return content;
    }

    public static ArrayList<LatLongDto> decodePoly(String encoded) {
        ArrayList<LatLongDto> poly = new ArrayList<LatLongDto>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLongDto p = new LatLongDto((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    public static float calcRouteDistance(StepDto[] steps) {
        float distance = 0;
        for (StepDto step : steps) {
            distance += step.getDistanceMeters();
        }
        return distance;
    }

    public static float calcRouteConsumption(VehicleDto vehicleDto, StepDto[] steps) {
        float distance = calcRouteDistance(steps);
        return (float) (distance * (vehicleDto.getType().getFuelConsumption() / 1000.0));
    }

    public static String getRouteData(Coord origin, Coord destination) {
        String content = "{\"origin\":{\"location\":{\"latLng\":{\"latitude\":%s,\"longitude\":%s}}},\"destination\":{\"location\":{\"latLng\":{\"latitude\":%s,\"longitude\":%s}}},\"travelMode\":\"DRIVE\",\"routingPreference\":\"TRAFFIC_AWARE\",\"computeAlternativeRoutes\":false,\"routeModifiers\":{\"avoidTolls\":false,\"avoidHighways\":false,\"avoidFerries\":false},\"languageCode\":\"en-US\",\"units\":\"IMPERIAL\"}";
        return String.format(content, origin.getLat(), origin.getLon(), destination.getLat(), destination.getLon());
    }

    public static StepDto[] getRoute(Coord start, Coord end) {
        String data = getRouteData(start, end);
        String url = "https://routes.googleapis.com/directions/v2:computeRoutes";
        System.out.println(url);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
//        headers.add("Accept", "*/*");
        headers.add("X-Goog-Api-Key", google_route_api_key);
        String[] filters = "routes.legs.steps".split(",");
        for (String filter : filters) {
            headers.add("X-Goog-FieldMask", filter);
        }
//        headers.add("X-Goog-FieldMask", "*");
        String content = makeRawRequest(url, HttpMethod.POST, data, String.class, headers);

        ObjectMapper om = new ObjectMapper();
        TrajetDto root = null;
        try {
            root = om.readValue(content, TrajetDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        TrajetDto trajetDto = new TrajetDto();
//        BeanUtils.copyProperties(content, trajetDto);
        info("--------------------");
        info(root);
        info("--------------------");
        StepDto[] steps = root.getRoutes()[0].getLegs()[0].getSteps();
        return steps;
    }

    /**
     * @param <T> Response type
     * @param <U> body type as dtos
     */

    public static <T, U> T makeRequest(String url, Class<T> responseType) {
        return RequestsUtils.makeRequest(url, HttpMethod.GET, null, responseType);
    }

    public static <T, U> T makeRequest(String url, HttpMethod method, U body, Class<T> responseType) {
        return makeRequest(url, method, body, responseType, null);
    }

    public static <T, U> T makeRequest(String url, HttpMethod method, U body, Class<T> responseType, MultiValueMap<String, String> headers) {
        return makeRawRequest(baseUrl + url, method, body, responseType, headers);
    }

    public static <T, U> T makeRawRequest(String url, HttpMethod method, U body, Class<T> responseType, MultiValueMap<String, String> headers) {
        RestTemplate restTemplate = new RestTemplate();
        //set interceptors/requestFactory
        ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
        List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
        ris.add(ri);
        restTemplate.setInterceptors(ris);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        try {
            HttpEntity<U> request = new HttpEntity<U>(body, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, method, request, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                T responseBody = response.getBody();
//                System.out.println(responseBody);
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

    public static double getDistanceBetweenCoord(Coord c1, Coord c2) {
        return GeoUtils.getDistance(c1, c2);
    }
    public static double getDistanceBetweenCoord(double lat1, double lon1, double lat2, double lon2) {
        return getDistanceBetweenCoord(new Coord(lon1, lat1), new Coord(lon2, lat2));
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
        return makeRequest("/vehicle/" + id, VehicleDto.class);
    }

    public static VehicleDto[] getAllVehicles() {
        return makeRequest("/vehicles", VehicleDto[].class);
    }

    public static boolean delAllVehicles() {
        return Boolean.TRUE.equals(makeRequest("/vehicle" + uuid, HttpMethod.DELETE, null, Boolean.class));
    }

    public static boolean delVehicle(int id) {
        return Boolean.TRUE.equals(makeRequest("/vehicle/" + uuid + '/' + id, HttpMethod.DELETE, null, Boolean.class));
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
        return makeRequest("/vehicle/move/" + uuid + "/" + id, HttpMethod.PUT, CoordLite.fromCoord(coord), VehicleDto.class);
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

    public static void info(Object message) {
        System.out.println(message);
    }

    public static void tests() {
        info(google_route_api_key);

        FacilityDto[] facilities = getFacilities();
        info(facilities.length + " facilities");
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
        RequestsUtils.info(fires.length + " fires");
        FireDto fakeFireDto = generateFakeFire(getCoord(otherFacility.getLon(), otherFacility.getLat()));
        Fire fakeFire = Fire.fromDto(fakeFireDto);
        for (FireDto fire : fires) {

        }
        VehicleDto truckDto = getVehicle(3799);
        Vehicle truck = Vehicle.fromDto(truckDto);
        RequestsUtils.info("truck 1: " + truck);
        //        int count = 0;
//        for (StepDto step : steps) {
//            info("Step " + count);
//            count++;
//            ArrayList<LatLongDto> decoded = decodePoly(step.getPolyline().getEncodedPolyline());
//            info("  decoded: " + decoded.size());
//        }
//        if (true) {
//            return;
//        }
        boolean running = true;
        VehicleDto vehicleDestination = getVehicle(12);
        while (running) {
            StepDto[] steps = getRoute(new Coord(truck.getLon(), truck.getLat()), new Coord(vehicleDestination.getLon(), vehicleDestination.getLat()));
            info("Go to destination");
            info("Distance du trajet: " + calcRouteDistance(steps) + "m");
            float conso = calcRouteConsumption(truckDto, steps);
            info("Conso: " + conso + "L");
            followSteps(truck.getId(), steps);
            truckDto = getVehicle(3799);
            truckDto.setFuel(truckDto.getFuel() - conso);
            updateVehicle(truckDto.getId(), truckDto);
            VehicleDto truckDto2 = getVehicle(3799);
            steps = getRoute(new Coord(truckDto2.getLon(), truckDto2.getLat()), new Coord(ownedFacilityDto.getLon(), ownedFacilityDto.getLat()));

            info("Return to base");
            info("Distance du trajet: " + calcRouteDistance(steps) + "m");
            info("Conso: " + calcRouteConsumption(truckDto, steps) + "L");
            followSteps(truck.getId(), steps);
            truckDto = getVehicle(3799);
            Vehicle vehicle = Vehicle.fromDto(truckDto);
            vehicle.reset();
            running = false;
        }
        if (true) {
            return;
        }
        moveToCoordInThread(new Coord(fakeFire.getLon(), fakeFire.getLat()), truckDto);
        truckDto = getVehicle(3799);
        moveToCoordInThread(new Coord(ownedFacility.getLon(), ownedFacility.getLat()), truckDto);
    }

    public static void main(String[] args) {
        testBots();
    }

    public static void waitFireOff(int fireId) {
        FireDto fireDto = getFire(fireId);
        while (fireDto.getIntensity() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fireDto = getFire(fireId);
        }
    }

    public static void followSteps(int id, StepDto[] stepDtos) {
        int count = 0;
        for (StepDto stepDto : stepDtos) {
            info("Step " + count);
            count++;
            List<LatLongDto> latLongDtos = new ArrayList<>();
            latLongDtos.add(stepDto.getStartLocation().getLatLong());
            latLongDtos.addAll(decodePoly(stepDto.getPolyline().getEncodedPolyline()));
            latLongDtos.add(stepDto.getEndLocation().getLatLong());
            for (LatLongDto latLongDto : latLongDtos) {
                VehicleDto vehicleDto = getVehicle(id);
                moveToCoordInThread(new Coord(latLongDto.getLongitude(), latLongDto.getLatitude()), vehicleDto);
            }
//            VehicleDto vehicleDto = getVehicle(id);
//            LatLongDto endLatLong = stepDto.getEndLocation().getLatLong();
//            moveToCoordInThread(new Coord(endLatLong.getLongitude(), endLatLong.getLatitude()), vehicleDto);
        }
    }

    public static void testBots() {
        VehicleDto truckDto = getVehicle(3799);
        FacilityDto ownedFacilityDto = getFacility(truckDto.getFacilityRefID());

        //FacilityDto cibledFacility = getFacility(6);

        //Trajet allee = new Trajet(0, new Coord(truckDto.getLon(), truckDto.getLat()), new Coord(cibledFacility.getLon(), cibledFacility.getLat()));
        Bot bot = BotManager.getInstance().createBot(truckDto);
        //test bot avce vrai coord de feu
        bot.setTrajet(new Trajet(0, new Coord(2.3488, 48.8534), new Coord(2.3488, 48.8534)));
        try {
            while(bot.getTrajet() != null) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        info("-------------------");
        info("-------------------");
        info("-------------------");
        info("-------------------");
        info("-------------------");
        info("-------------------");
        info("-------------------");
        info("-------------------");
        Trajet retour = new Trajet(0, new Coord(bot.getVehicle().getLon(), bot.getVehicle().getLat()), new Coord(ownedFacilityDto.getLon(), ownedFacilityDto.getLat()));
        bot.setTrajet(retour);
        try {
            while(bot.getTrajet() != null) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        bot.getVehicle().setOptimizedLiquid(FireType.D_Metals);
        updateVehicle(bot.getVehicle().getId(), bot.getVehicle().toDto());
    }

    public static void moveToCoordInThread(Coord fire, VehicleDto vehicleDto) {
        // Coefficient accelerateur de mouvement
        float speed_coef = 3.0f;
        // request delay in milliseconds
        float request_rate = 0.4f;
        double start_distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
        // Ligne droite
        double vector_x = fire.getLat() - vehicleDto.getLat();
        double vector_y = fire.getLon() - vehicleDto.getLon();

        // time in seconds
        float speed = vehicleDto.getType().getMaxSpeed();
        double time = (1 / request_rate) * (3600) * ((start_distance / 1000.0) / (speed)) / speed_coef;
        double step_x = vector_x / time;
        double step_y = vector_y / time;
//        info("step_x : " + step_x);
//        info("step_y : " + step_y);
        double step_distance = getDistanceBetweenCoord(step_x, step_y, 0, 0);
        double new_lat;
        double new_lon;
        double distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
        while (distance > step_distance) {
            new_lat = vehicleDto.getLat() + step_x;
            new_lon = vehicleDto.getLon() + step_y;
            vehicleDto.setLat(new_lat);
            vehicleDto.setLon(new_lon);
            distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
            if (distance >= step_distance) {
                moveVehicle(vehicleDto.getId(), vehicleDto);
            }
//            info("  distance : " + distance);
            try {
                Thread.sleep((long) (1000 * request_rate));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        new_lat = fire.getLat();
        new_lon = fire.getLon();
        vehicleDto.setLat(new_lat);
        vehicleDto.setLon(new_lon);
        distance = getDistanceBetweenCoord(fire.getLat(), fire.getLon(), vehicleDto.getLat(), vehicleDto.getLon());
        moveVehicle(vehicleDto.getId(), vehicleDto);
//        info("  distance : " + distance);
    }

}
