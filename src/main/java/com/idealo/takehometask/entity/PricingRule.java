package com.idealo.takehometask.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pricing_rule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class PricingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;
    private Integer upperThreshold;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "sku_pricing_rule",
            joinColumns = @JoinColumn(name = "pricing_rule_id", referencedColumnName = "id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "sku_id", referencedColumnName = "id", nullable = true))
    @JsonIgnore
    @Column(nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Sku> skus;
    private Double totalValue;

}
