package com.idealo.takehometask.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "checkout")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Checkout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;
    @OneToMany(mappedBy = "checkout", orphanRemoval = false,
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @Column(nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Sku> skuList;
}
