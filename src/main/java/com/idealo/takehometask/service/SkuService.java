package com.idealo.takehometask.service;

import com.idealo.takehometask.dao.SkuDao;
import com.idealo.takehometask.entity.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SkuService {
    @Autowired
    SkuDao skuDao;

    public Sku createSku(Sku sku) {
        return skuDao.save(sku);
    }

    public List<Sku> getSkus() {
        return skuDao.findAll();
    }

    public Optional<Sku> getSkuById(UUID id) {
        return skuDao.findById(id);
    }

    public Optional<Object> deleteSku(UUID id) {
        if (!skuDao.existsById(id))
            return Optional.empty();

        var sku = skuDao.findById(id);
        if (sku.get().getPricingRuleList().size() != 0)
            return Optional.empty();

        skuDao.delete(sku.get());
        return Optional.of(true);
    }
}
