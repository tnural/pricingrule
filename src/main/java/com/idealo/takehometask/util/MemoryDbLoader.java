package com.idealo.takehometask.util;

import com.idealo.takehometask.dao.CheckoutDao;
import com.idealo.takehometask.dao.PricingRuleDao;
import com.idealo.takehometask.dao.SkuDao;
import com.idealo.takehometask.entity.Checkout;
import com.idealo.takehometask.entity.PricingRule;
import com.idealo.takehometask.entity.Sku;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@Getter
@Setter
public class MemoryDbLoader {
    @Autowired
    CheckoutDao checkoutDao;
    @Autowired
    PricingRuleDao pricingRuleDao;
    @Autowired
    SkuDao skuDao;
    Checkout checkout;
    List<Sku> skuList;
    List<PricingRule> pricingRuleList;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        var c = checkoutDao.save(Checkout.builder().build());
        this.checkout = c;
        var skuList = Set.of(
                Sku.builder().id(UUID.randomUUID()).name("A").price(40.0).checkout(c).build(),
                Sku.builder().id(UUID.randomUUID()).name("A").price(40.0).checkout(c).build(),
                Sku.builder().id(UUID.randomUUID()).name("A").price(40.0).checkout(c).build(),
                Sku.builder().id(UUID.randomUUID()).name("B").price(50.0).checkout(c).build(),
                Sku.builder().id(UUID.randomUUID()).name("B").price(50.0).checkout(c).build(),
                Sku.builder().id(UUID.randomUUID()).name("C").price(25.0).checkout(c).build(),
                Sku.builder().id(UUID.randomUUID()).name("D").price(20.0).checkout(c).build()
        );
        skuDao.saveAll(skuList);
        this.skuList = skuDao.findAll();
        var skusNamedA = skuDao.findByName("A");
        var skusNamedB = skuDao.findByName("B");
        var pricingRuleList = Set.of(
                PricingRule.builder().id(UUID.randomUUID())
                        .skus(skusNamedA)
                        .upperThreshold(3)
                        .totalValue(100.0).build(),
                PricingRule.builder().id(UUID.randomUUID())
                        .skus(skusNamedB)
                        .upperThreshold(2)
                        .totalValue(80.0).build()
        );
        pricingRuleDao.saveAll(pricingRuleList);
        this.pricingRuleList = pricingRuleDao.findAll();
    }
}
