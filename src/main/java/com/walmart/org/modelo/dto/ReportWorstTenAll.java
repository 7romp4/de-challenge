package com.walmart.org.modelo.dto;

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

import com.walmart.org.modelo.Console;
import com.walmart.org.modelo.Score;
import com.walmart.org.repo.ConsoleRepo;
import com.walmart.org.repo.ScoreRepo;

public class ReportWorstTenAll implements Tasklet,StepExecutionListener {
	
	@Autowired
	private ScoreRepo scoreRepo;	
	
	private List<Score> scores;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		scores = scoreRepo.findAll();
		
		System.out.println("========= WORST 10 GAMES ALL =======");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<Score> filtrado = scores
				.stream()
				.sorted(Comparator.comparing(Score::getMetascore))
				.collect(Collectors.toList())
				.subList(0, 10);
		filtrado.forEach(filtro ->{
			System.out.println(filtro.getMetascore()+" - "+filtro.getName()+" - "+filtro.getConsole().getName());
		});
		return null;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}
}
