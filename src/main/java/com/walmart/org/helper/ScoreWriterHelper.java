package com.walmart.org.helper;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.walmart.org.modelo.Score;
import com.walmart.org.modelo.dto.ScoreWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScoreWriterHelper {

	public static ScoreWriter scoreToSw(Score score) {
		ScoreWriter sw = new ScoreWriter(score.getId(),score.getMetascore(),score.getName(),score.getConsole().getName());
		log.info(score.getId()+" - "+score.getMetascore()+" - "+score.getName()+" - "+score.getConsole().getName());
		return sw;
	}
	
	public static void writeToCSV(String route, List<ScoreWriter> scoresWriter) throws IOException {
		Writer writer = Files.newBufferedWriter(Paths.get(route));
		
		StatefulBeanToCsv<ScoreWriter> csvWriter = new StatefulBeanToCsvBuilder<ScoreWriter>(writer)
	            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
	            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
	            .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
	            .withLineEnd(CSVWriter.DEFAULT_LINE_END)
	            .build();
		
		try {
			csvWriter.write(scoresWriter);
			writer.close();
		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e ) {
			log.error("Error al generar CSV ",e);
		}
	}
}
