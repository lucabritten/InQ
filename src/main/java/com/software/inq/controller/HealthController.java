package com.software.inq.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health-Check")
public class HealthController {

    @Operation(summary = "Check if application runs")
    @GetMapping
    public ResponseEntity<String> health(){
        return ResponseEntity.ok("OK");
    }
}
