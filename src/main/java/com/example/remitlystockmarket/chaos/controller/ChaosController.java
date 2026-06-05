package com.example.remitlystockmarket.chaos.controller;

import com.example.remitlystockmarket.chaos.service.ChaosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chaos")
@Tag(name = "Chaos Controller", description = "Endpoints for simulating chaos scenarios, such as killing a serving instance")
public class ChaosController {

    private final ChaosService chaosService;

    public ChaosController(ChaosService chaosService) {
        this.chaosService = chaosService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kill Serving Instance", description = "Terminates the currently serving instance")
    @ApiResponse(responseCode = "200", description = "Successfully terminated the serving instance")
    public ResponseEntity<Map<String, String>> killServingInstance() {
        String instance = chaosService.killCurrentInstance();
        return ResponseEntity.ok(Map.of(
                "message", "Terminating instance:",
                "instance", instance
        ));
    }
}
