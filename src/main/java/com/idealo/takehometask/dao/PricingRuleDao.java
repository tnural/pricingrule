package com.idealo.takehometask.dao;

import com.idealo.takehometask.entity.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface PricingRuleDao extends JpaRepository<PricingRule, UUID> {
}
