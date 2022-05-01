package com.idealo.takehometask.controller;

import com.idealo.takehometask.entity.Checkout;
import com.idealo.takehometask.service.CheckoutService;
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
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    CheckoutService checkoutService;

    @GetMapping
    public ResponseEntity<List<Checkout>> getCheckouts() {
        return new ResponseEntity<>(checkoutService.getCheckouts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public HttpEntity<? extends Object> getCheckoutById(@PathVariable UUID id) {
        var checkoutPersist = checkoutService.getCheckoutById(id);
        if (checkoutPersist.isEmpty())
            return ResponseEntity.unprocessableEntity().build();

        return Optional
                .ofNullable(checkoutPersist)
                .map(checkout -> ResponseEntity.ok().body(checkout)).get();
    }

    @PutMapping
    public HttpEntity<?> modifyCheckout(@RequestBody Checkout checkout) {
        var checkoutPersist = checkoutService.modifyCheckout(checkout);
        if (checkoutPersist.isEmpty())
            return ResponseEntity.unprocessableEntity().build();

        return checkoutPersist.map(c -> ResponseEntity.ok().body(c)).get();
    }

    @PostMapping
    public ResponseEntity<Checkout> createCheckout(@RequestBody Checkout checkout) {
        return Optional
                .ofNullable(checkoutService.createCheckout(checkout))
                .map(ck -> ResponseEntity.ok().body(ck)).get();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePost(@PathVariable UUID id) {
        var checkout = checkoutService.deleteCheckout(id);
        if (checkout.isEmpty())
            return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().body("Checkout has been deleted.");
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<Object> processCheckoutById(@PathVariable UUID id) {
        var resp = checkoutService.processCheckoutById(id);
        if (resp.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().body(resp.get());
    }

}
