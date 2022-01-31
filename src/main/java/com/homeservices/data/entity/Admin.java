package com.homeservices.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "admin")
public class Admin extends Users
{
}
