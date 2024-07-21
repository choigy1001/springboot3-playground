package com.easy.springBoot3.domain.beer.repository;

import com.easy.springBoot3.domain.beer.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, Long> {
}
