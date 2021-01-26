package com.walmart.org.modelo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Company {


    @Id
    private Long id;
    private String name;
}
