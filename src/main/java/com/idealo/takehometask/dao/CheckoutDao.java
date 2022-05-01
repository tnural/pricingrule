package com.idealo.takehometask.dao;

import com.idealo.takehometask.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface CheckoutDao extends JpaRepository<Checkout, UUID> {
}
