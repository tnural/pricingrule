package com.idealo.takehometask.controller;

import com.idealo.takehometask.entity.Sku;
import com.idealo.takehometask.service.SkuService;
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
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    SkuService skuService;

    @GetMapping
    public ResponseEntity<List<Sku>> getSkus() {
        return new ResponseEntity<>(skuService.getSkus(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public HttpEntity<? extends Object> getSkuById(@PathVariable UUID id) {
        var sku = skuService.getSkuById(id);
        if (sku.isEmpty())
            return ResponseEntity.unprocessableEntity().build();

        return Optional
                .ofNullable(sku)
                .map(pricingRule -> ResponseEntity.ok().body(pricingRule)).get();
    }

    @PostMapping
    public ResponseEntity<Sku> createSku(@RequestBody Sku sku) {
        return Optional
                .ofNullable(skuService.createSku(sku))
                .map(skl -> ResponseEntity.ok().body(skl)).get();

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSku(@PathVariable UUID id) {
        var skl = skuService.deleteSku(id);
        if (skl.isEmpty())
            return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().body("Sku has been deleted.");
    }

}
