package com.homeservices.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "experts")
public class Experts extends Users
{
    @Lob
    @Column(name = "expert_image")
    private byte[] img;

    private int rating;

    private String areaOfExpertise;
}
