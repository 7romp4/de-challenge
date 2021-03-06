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
import com.walmart.org.modelo.Console;
import com.walmart.org.modelo.Score;
import com.walmart.org.repo.ConsoleRepo;
import com.walmart.org.repo.ScoreRepo;

public class ReportWorstTenConsole implements Tasklet,StepExecutionListener {
	
	private static final String ROUTE = "worst10gamesconsole.csv";
	
	@Autowired
	private ScoreRepo scoreRepo;
	
	@Autowired
	private ConsoleRepo consoleRepo;
	
	
	private List<Console> consoles;
	private List<Score> scores;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		consoles = consoleRepo.findAll();
		scores = scoreRepo.findAll();
		
		System.out.println("========= WORST 10 GAMES =======");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		List<ScoreWriter> scoresWriter = new ArrayList<>();
		
		consoles.forEach(console -> {
			List<Score> filtrado = scores
					.stream()
					.filter(score -> console.getId().equals(score.getConsole().getId()))
					.sorted(Comparator.comparing(Score::getMetascore))
					.collect(Collectors.toList())
					.subList(0, 10);
			filtrado.forEach(filtro ->{
				scoresWriter.add(ScoreWriterHelper.scoreToSw(filtro));
			});
		});
		ScoreWriterHelper.writeToCSV(ROUTE, scoresWriter);
		return null;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}
}
