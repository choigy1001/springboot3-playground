package com.easy.springBoot3.chained.controller;

import com.easy.springBoot3.chained.service.ChainedService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chained")
@Profile("chained")
public class ChainedController {
    private final ChainedService chainedService;

    @PostMapping
    public ResponseEntity<?> createChainedBeerAndStudy() {
        chainedService.createChainedBeerAndStudy();
        return ResponseEntity.ok().build();
    }
}
