package com.easy.springBoot3.domain.study.repository;

import com.easy.springBoot3.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
