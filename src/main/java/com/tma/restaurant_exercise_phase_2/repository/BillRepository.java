package com.tma.restaurant_exercise_phase_2.repository;

import com.tma.restaurant_exercise_phase_2.model.bill.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT b FROM Bill b")
    Page<Bill> getAllBills(Pageable pageable);
}
