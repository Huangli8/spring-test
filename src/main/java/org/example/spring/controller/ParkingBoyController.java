package org.example.spring.controller;

import org.example.spring.entity.ParkingBoy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ParkingBoyController {
   private final Map<String, ParkingBoy> database = new ConcurrentHashMap<>();

   @PostMapping("/parking-boys")
    public ResponseEntity<ParkingBoy> create(@RequestBody ParkingBoy parkingBoy) {
       if(parkingBoy.getId()==null ||parkingBoy.getId().isBlank())
           return ResponseEntity.badRequest().build();
       database.put(parkingBoy.getId(), parkingBoy);
       return ResponseEntity.status(HttpStatus.CREATED).body(parkingBoy);
   }

}
