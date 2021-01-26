package com.walmart.org.repo;

import com.walmart.org.modelo.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepo extends JpaRepository<Score,Long> {
}
