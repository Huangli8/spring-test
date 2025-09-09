package org.example.spring;

import org.apache.coyote.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/parking-boys")
    public void getParkingBoyInfo() {

    }
    @PostMapping("/parking-boys/parking-lots/parked-cars")
    public void createParkedCars() {

    }
}
