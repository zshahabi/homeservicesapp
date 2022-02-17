package com.home.services.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "admin")
public class Admin extends User
{
}
