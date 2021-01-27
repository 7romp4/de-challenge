package com.walmart.org.modelo.dto;


import com.opencsv.bean.CsvBindByPosition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ScoreWriter{

	@CsvBindByPosition(position = 0)
	Long id;
	@CsvBindByPosition(position = 1)
	Integer metaScore;
	@CsvBindByPosition(position = 2)
    String name;
	@CsvBindByPosition(position = 3)
    String console;
}
