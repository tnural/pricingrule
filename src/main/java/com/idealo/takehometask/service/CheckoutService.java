package com.idealo.takehometask.service;

import com.idealo.takehometask.entity.PricingRule;
import com.idealo.takehometask.entity.Sku;
import com.idealo.takehometask.entity.Checkout;
import com.idealo.takehometask.dao.CheckoutDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    @Autowired
    CheckoutDao checkoutDao;

    public List<Checkout> getCheckouts() {
        return checkoutDao.findAll();
    }

    public Optional<Checkout> getCheckoutById(UUID id) {
        return checkoutDao.findById(id);
    }

    public Checkout createCheckout(Checkout checkout) {
        return checkoutDao.save(checkout);
    }

    public Optional<? extends Object> modifyCheckout(Checkout checkout) {
        if (!checkoutDao.existsById(checkout.getId()))
            return Optional.empty();

        return Optional
                .ofNullable(checkoutDao.save(checkout));
    }

    public Optional<Object> processCheckoutById(UUID id) {
        var totalVal = 0.0;
        if (!checkoutDao.existsById(id))
            return Optional.empty();

        var checkout = checkoutDao.findById(id);
        var skuCountByName =
                checkout.get()
                        .getSkuList()
                        .stream()
                        .collect(Collectors.groupingBy(Sku::getName, Collectors.counting()));

        for (var entry : skuCountByName.entrySet()) {
            var skuName = entry.getKey();
            var skuCount = entry.getValue();
            var sku = checkout.get().getSkuList()
                    .stream()
                    .filter(s -> s.getName().equals(skuName))
                    .findFirst()
                    .get();
            var skuPricingRule =
                    sku.getPricingRuleList()
                            .stream()
                            .filter((pr) -> skuCount >= pr.getUpperThreshold())
                            .max(Comparator.comparing(PricingRule::getUpperThreshold));
            if (skuPricingRule.isPresent()) {
                totalVal += skuPricingRule.get().getTotalValue() + sku.getPrice() * (skuCount - skuPricingRule.get().getUpperThreshold());
            } else {
                totalVal += sku.getPrice() * skuCount;
            }
        }
        return Optional.of(totalVal);
    }

    public Optional<Object> deleteCheckout(UUID id) {
        if (!checkoutDao.existsById(id))
            return Optional.empty();

        var checkout = checkoutDao.findById(id);
        checkoutDao.delete(checkout.get());
        return Optional.of(true);
    }
}
