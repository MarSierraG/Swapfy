package com.swapfy.backend.repositories;

import com.swapfy.backend.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Métodos adicionales si los necesitas, por ejemplo:
    // List<Transaction> findByStatus(String status);
}
