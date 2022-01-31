package com.homeservices.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "service_order")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    private String description;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "request_at", nullable = false, updatable = false)
    private LocalDateTime requestAt;

    @ManyToOne
    @JoinColumn(name = "sub_service_id", referencedColumnName = "id")
    private SubService subService;

    @ManyToOne
    @JoinColumn(name = "expert_id", referencedColumnName = "id")
    private Experts experts;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
}
