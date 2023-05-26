package fr.clbd.fire.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class requests {
    final static String baseUrl = "http://vps.cpe-sn.fr:8081/";

    public static String makeRequest(String url, HttpMethod method, HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + url, method, entity, String.class);
        return response.getBody();
    }

}
