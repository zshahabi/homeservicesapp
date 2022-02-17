package com.home.services.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "experts")
public class Expert extends User
{
    @Lob
    @Column(name = "expert_image")
    private byte[] img;

    private int rating;

    private String areaOfExpertise;

    @ManyToMany
    private Set<SubService> subServices = new HashSet<>();
}
