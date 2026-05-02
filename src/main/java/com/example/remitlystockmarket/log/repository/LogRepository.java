package com.example.remitlystockmarket.log.repository;

import com.example.remitlystockmarket.log.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
