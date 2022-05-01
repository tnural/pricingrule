package com.idealo.takehometask.controller;

import com.idealo.takehometask.entity.PricingRule;
import com.idealo.takehometask.service.PricingRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("pricingrule")
public class PricingRuleController {

    @Autowired
    PricingRuleService pricingRuleService;

    @GetMapping
    public ResponseEntity<List<PricingRule>> getPricingRules() {
        return new ResponseEntity<>(pricingRuleService.getPricingRules(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public HttpEntity<? extends Object> getPricingRuleById(@PathVariable UUID id) {
        var pr = pricingRuleService.getPricingRuleById(id);
        if (pr.isEmpty())
            return ResponseEntity.unprocessableEntity().build();

        return Optional
                .ofNullable(pr)
                .map(pricingRule -> ResponseEntity.ok().body(pricingRule)).get();
    }

    @PostMapping
    public ResponseEntity<PricingRule> createPricingRule(@RequestBody PricingRule pricingRule) {
        return Optional
                .ofNullable(pricingRuleService.createPricingRule(pricingRule))
                .map(pr -> ResponseEntity.ok().body(pr)).get();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePricingRule(@PathVariable UUID id) {
        var pr = pricingRuleService.deletePricingRule(id);
        if (pr.isEmpty())
            return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().body("Pricing rule has been deleted.");
    }
}
