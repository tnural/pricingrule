package com.idealo.takehometask.service;

import com.idealo.takehometask.dao.PricingRuleDao;
import com.idealo.takehometask.entity.PricingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PricingRuleService {

    @Autowired
    PricingRuleDao pricingRuleDao;

    public List<PricingRule> getPricingRules() {
        return pricingRuleDao.findAll();
    }

    public Optional<PricingRule> getPricingRuleById(UUID id) {
        return pricingRuleDao.findById(id);
    }

    public PricingRule createPricingRule(PricingRule pricingRule) {
        return pricingRuleDao.save(pricingRule);
    }

    public Optional<Object> deletePricingRule(UUID id) {
        if (!pricingRuleDao.existsById(id))
            return Optional.empty();

        var pr = pricingRuleDao.findById(id);
        pricingRuleDao.delete(pr.get());
        return Optional.of(true);

    }
}
