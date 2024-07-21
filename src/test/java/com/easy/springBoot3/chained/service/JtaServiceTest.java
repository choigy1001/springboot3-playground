package com.easy.springBoot3.chained.service;

import com.easy.springBoot3.domain.beer.repository.BeerRepository;
import com.easy.springBoot3.domain.study.repository.StudyRepository;
import com.easy.springBoot3.jta.service.JtaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JtaServiceTest {

    @Autowired
    private JtaService jtaService;
    @Autowired
    private BeerRepository beerRepository;
    @Autowired
    private StudyRepository studyRepository;

    @Test
    @DisplayName("예외가 발생하면 모든 트랜잭션을 롤백 시킨다")
    void createChainedBeerAndStudy() {
        assertThatThrownBy(() -> jtaService.saveBeerAndStudy())
                .isInstanceOf(RuntimeException.class);
        assertThat(beerRepository.findAll().size()).isEqualTo(0);
        assertThat(studyRepository.findAll().size()).isEqualTo(0);
    }
}