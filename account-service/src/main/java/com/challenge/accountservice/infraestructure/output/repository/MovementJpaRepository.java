package com.challenge.accountservice.infraestructure.output.repository;

import com.challenge.accountservice.infraestructure.output.repository.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long> {
    List<MovementEntity> findByAccountId(Long accountId);

    @Query("SELECT m.balance FROM MovementEntity m WHERE m.account.accountNumber = :accountNumber ORDER BY m.date DESC, m.id DESC")
    Optional<Double> findLastBalanceByAccountNumber(@Param("accountNumber") String accountNumber);
}
