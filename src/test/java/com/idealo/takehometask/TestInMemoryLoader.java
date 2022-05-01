package com.idealo.takehometask;

import com.idealo.takehometask.dao.CheckoutDao;
import com.idealo.takehometask.dao.PricingRuleDao;
import com.idealo.takehometask.dao.SkuDao;
import com.idealo.takehometask.entity.Checkout;
import com.idealo.takehometask.entity.PricingRule;
import com.idealo.takehometask.entity.Sku;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Getter
@Setter
public class TestInMemoryLoader {

    @Autowired
    CheckoutDao checkoutDao;
    @Autowired
    PricingRuleDao pricingRuleDao;
    @Autowired
    SkuDao skuDao;
    private Checkout checkout;
    private List<Sku> skuList;
    private List<PricingRule> pricingRuleList;

    public void fillUp() {
        var c = checkoutDao.save(Checkout.builder().build());
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
        this.checkout = this.skuList.get(0).getCheckout();
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

    public void empty() {
        this.pricingRuleDao.deleteAll();
        this.skuDao.deleteAll();
        this.checkoutDao.deleteAll();
    }

}
