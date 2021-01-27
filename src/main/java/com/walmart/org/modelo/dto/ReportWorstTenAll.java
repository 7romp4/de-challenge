package com.walmart.org.modelo.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.walmart.org.helper.ScoreWriterHelper;
import com.walmart.org.modelo.Score;
import com.walmart.org.repo.ScoreRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportWorstTenAll implements Tasklet,StepExecutionListener {
	
	private static final String ROUTE = "worst10gamesall.csv";
	
	@Autowired
	private ScoreRepo scoreRepo;	
	
	private List<Score> scores;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		scores = scoreRepo.findAll();
		
		log.info("========= WORST 10 GAMES ALL =======");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		List<ScoreWriter> scoresWriter = new ArrayList<>();
		
		List<Score> filtrado = scores
				.stream()
				.sorted(Comparator.comparing(Score::getMetascore))
				.collect(Collectors.toList())
				.subList(0, 10);
		filtrado.forEach(filtro ->{
			scoresWriter.add(ScoreWriterHelper.scoreToSw(filtro));
		});
		ScoreWriterHelper.writeToCSV(ROUTE, scoresWriter);
		return null;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}
}
