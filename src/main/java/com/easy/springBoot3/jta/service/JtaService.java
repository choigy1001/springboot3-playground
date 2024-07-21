package com.easy.springBoot3.jta.service;

import com.easy.springBoot3.domain.beer.entity.Beer;
import com.easy.springBoot3.domain.beer.repository.BeerRepository;
import com.easy.springBoot3.domain.study.entity.Study;
import com.easy.springBoot3.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JtaService {
    private final BeerRepository beerRepository;
    private final StudyRepository studyRepository;

    @Transactional(transactionManager = "atomikosTransactionManager")
    public void saveBeerAndStudy() {
        Beer beer = Beer.of("Jta Beer", 10);
        beerRepository.save(beer);

        Study study = Study.from("Jta");
        studyRepository.save(study);
        if (1 == 1) {
            throw new RuntimeException();
        }
    }
}
