package org.example.ecommerce.controller.seller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@CrossOrigin(origins = "http://192.168.233.1:3000")
@RequestMapping("/registration")
public class RegistrationController {
    @Value("${location.api.token}")
    private String mapApiKey;
    @GetMapping("/api/get-address")
    public ResponseEntity<String> getAddress(@RequestParam double lat, @RequestParam double lng) {
        try {
            String url = String.format(
//                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s&language=vi",
                    "https://us1.locationiq.com/v1/reverse?key=%s&lat=%f&lon=%f&format=json&language=vi",
                    mapApiKey, lat, lng
            );
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(URI.create(url), String.class);
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

}
