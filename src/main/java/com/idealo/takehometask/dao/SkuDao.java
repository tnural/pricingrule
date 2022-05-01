package com.idealo.takehometask.dao;

import com.idealo.takehometask.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SkuDao extends JpaRepository<Sku, UUID> {
    List<Sku> findByName(String name);
}
