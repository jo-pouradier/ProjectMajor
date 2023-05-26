package fr.clbd.fire.utils;

import com.project.model.dto.FireDto;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class RequestsUtils {
    final static String baseUrl = "http://vps.cpe-sn.fr:8081/";
    /**
    * @param <T> Response type
    * @param <U> body type as dtos
    */


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
    
    public static FireDto getAllFires(){
        return null;
    }
}
