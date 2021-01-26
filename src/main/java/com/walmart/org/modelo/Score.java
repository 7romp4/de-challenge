package com.walmart.org.modelo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;

@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Score {

    @Id
    Long id;
    String name;
    Integer metascore;
    Float userscore;
    @OneToOne
    Console console;
    Date date;
}
