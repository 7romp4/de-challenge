package com.walmart.org.modelo.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreReader {

    Integer metaScore;
    String name;
    String console;
    String userScore;
    Date date;
}
