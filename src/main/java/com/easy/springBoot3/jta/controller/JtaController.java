package com.easy.springBoot3.jta.controller;

import com.easy.springBoot3.jta.service.JtaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jta")
public class JtaController {

    private final JtaService jtaService;

    @GetMapping
    public ResponseEntity<Void> saveBeerAndStudy() {
        jtaService.saveBeerAndStudy();
        return ResponseEntity.ok().build();
    }
}
