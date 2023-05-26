package fr.clbd.fire.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class requests {
    final String baseUrl = "http://vps.cpe-sn.fr:8081/";
    RestTemplate restTemplate = new RestTemplate();

    public String makeRequest(String url, HttpMethod method, HttpEntity<String> entity) {
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + url, method, entity, String.class);
        return response.getBody();
    }

}
