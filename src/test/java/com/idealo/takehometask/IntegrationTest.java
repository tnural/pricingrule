package com.idealo.takehometask;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idealo.takehometask.entity.Checkout;
import com.idealo.takehometask.entity.PricingRule;
import com.idealo.takehometask.entity.Sku;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestInMemoryLoader testInMemoryLoader;

    @BeforeEach
    void init() {
        this.testInMemoryLoader.fillUp();
    }

    @AfterEach
    void destruct() {
        this.testInMemoryLoader.empty();
    }

    @Test
    void assertSkuCount() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/sku"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(testInMemoryLoader.getSkuList().size())));
    }

    @Test
    void assertSkuGet() throws Exception {
        var sku = this.testInMemoryLoader.getSkuList().get(0);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/sku/" + sku.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(sku.getId().toString())));
    }

    @Test
    void assertSkuPost() throws Exception {
        var sku = Sku.builder().name("X").price(10.0).build();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/sku")
                                .content(asJsonString(sku))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void assertPricingRuleCount() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/pricingrule"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(testInMemoryLoader.getPricingRuleList().size())));
    }

    @Test
    void assertPricingRuleGet() throws Exception {
        var pr = this.testInMemoryLoader.getPricingRuleList().get(0);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/pricingrule/" + pr.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(pr.getId().toString())));
    }

    @Test
    void assertPricingRuleDelete() throws Exception {
        var pr = this.testInMemoryLoader.getPricingRuleList().get(0);
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/pricingrule/" + pr.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void assertPricingRulePost() throws Exception {
        var pricingRule = PricingRule.builder().upperThreshold(10).totalValue(100.0);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/pricingrule")
                                .content(asJsonString(pricingRule))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void assertCheckoutGet() throws Exception {
        var ch = this.testInMemoryLoader.getCheckout();
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/checkout/" + ch.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(ch.getId().toString())));
    }

    @Test
    void assertCheckoutDelete() throws Exception {
        var checkout = this.testInMemoryLoader.getCheckout();
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/checkout/" + checkout.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void assertCheckoutPut() throws Exception {
        var checkout = this.testInMemoryLoader.getCheckout();
        var skuList = checkout.getSkuList();
        skuList.remove(0);
        checkout.setSkuList(null);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/checkout")
                                .content(asJsonString(checkout))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(checkout.getId().toString())));

    }

    @Test
    void assertCheckoutPost() throws Exception {
        var checkout = Checkout.builder().build();
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/checkout")
                                .content(asJsonString(checkout))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void assertCheckoutCost() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/checkout/" + testInMemoryLoader.getCheckout().getId().toString() + "/process"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", is(225.0)));
    }

    public static String asJsonString(final Object obj) {
        try {
            var objMapper = new ObjectMapper();
            objMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            return objMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
