package com.idealo.takehometask;

import com.idealo.takehometask.entity.Checkout;
import com.idealo.takehometask.entity.PricingRule;
import com.idealo.takehometask.entity.Sku;
import com.idealo.takehometask.service.CheckoutService;
import com.idealo.takehometask.service.PricingRuleService;
import com.idealo.takehometask.service.SkuService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UnitTest {

    @Autowired
    TestInMemoryLoader testInMemoryLoader;

    @Autowired
    SkuService skuService;

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    PricingRuleService pricingRuleService;

    @BeforeEach
    void fillUpDb() {
        this.testInMemoryLoader.fillUp();
    }

    @AfterEach
    void emptyDb() {
        this.testInMemoryLoader.empty();
    }

    @Test
    void checkSkuSize() {
        assertEquals(testInMemoryLoader.getSkuList().size(), skuService.getSkus().size());
    }

    @Test
    void checkSkuById() {
        assertEquals(testInMemoryLoader.getSkuList().get(0).getId(), skuService.getSkuById(testInMemoryLoader.getSkuList().get(0).getId()).get().getId());
    }

    @Test
    void createSku() {
        var sku = Sku.builder().name("test-checkout").price(100.0).build();
        assertEquals(skuService.createSku(sku).getName(), sku.getName());
    }

    @Test
    void checkPricingRuleSize() {
        assertEquals(testInMemoryLoader.getPricingRuleList().size(), pricingRuleService.getPricingRules().size());
    }

    @Test
    void checkPricingRuleById() {
        assertEquals(testInMemoryLoader.getPricingRuleList().get(0).getId(), pricingRuleService.getPricingRuleById(testInMemoryLoader.getPricingRuleList().get(0).getId()).get().getId());
    }

    @Test
    void deletePricingRule() {
        assertEquals(pricingRuleService.deletePricingRule(testInMemoryLoader.getPricingRuleList().get(0).getId()), Optional.of(true));
    }

    @Test
    void createPricingRule() {
        var pr = PricingRule.builder().skus(testInMemoryLoader.getSkuList()).upperThreshold(5).totalValue(120.0).build();
        assertEquals(pricingRuleService.createPricingRule(pr).getTotalValue(), pr.getTotalValue());
    }

    @Test
    void checkCheckoutSize() {
        assertEquals(testInMemoryLoader.getCheckout().getId(), checkoutService.getCheckouts().get(0).getId());
    }

    @Test
    void deleteCheckout() {
        assertEquals(checkoutService.deleteCheckout(testInMemoryLoader.getCheckout().getId()), Optional.of(true));
    }

    @Test
    void createCheckout() {
        var ch = Checkout.builder().skuList(testInMemoryLoader.getSkuList()).build();
        assertEquals(checkoutService.createCheckout(ch).getSkuList().size(), ch.getSkuList().size());
    }

    @Test
    void calculateCostOfCheckout() {
        assertEquals(checkoutService.processCheckoutById(testInMemoryLoader.getCheckout().getId()).get(), 225.0);
    }
}
