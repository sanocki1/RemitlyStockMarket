package com.example.remitlystockmarket.chaos.controller;

import com.example.remitlystockmarket.chaos.service.ChaosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chaos")
public class ChaosController {

    private final ChaosService chaosService;

    public ChaosController(ChaosService chaosService) {
        this.chaosService = chaosService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> killServingInstance() {
        String instance = chaosService.killCurrentInstance();
        return ResponseEntity.ok(Map.of(
                "message", "Instance termination scheduled",
                "instance", instance
        ));
    }
}
