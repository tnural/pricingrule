package com.idealo.takehometask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sku")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String name;
    private Double price;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "checkout_id", nullable = true)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private Checkout checkout;
    @ManyToMany(mappedBy = "skus", cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Column(nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<PricingRule> pricingRuleList;
}
