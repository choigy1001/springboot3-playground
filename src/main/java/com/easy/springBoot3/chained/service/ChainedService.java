package com.easy.springBoot3.chained.service;

import com.easy.springBoot3.domain.beer.entity.Beer;
import com.easy.springBoot3.domain.beer.repository.BeerRepository;
import com.easy.springBoot3.domain.study.entity.Study;
import com.easy.springBoot3.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Profile("chained")
public class ChainedService {

    private final BeerRepository beerRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public void createChainedBeerAndStudy() {
        Beer beer = Beer.of("Sapporo", 100);
        beerRepository.save(beer);

        Study study = Study.from("Math");
        studyRepository.save(study);
        if (1 == 1) {
            throw new RuntimeException();
        }
    }
}
