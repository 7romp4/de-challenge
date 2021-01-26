package com.walmart.org.modelo.dto;

import com.walmart.org.modelo.Company;
import com.walmart.org.modelo.Console;
import com.walmart.org.modelo.Score;
import com.walmart.org.repo.ConsoleRepo;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreItemProcessor implements ItemProcessor<ScoreReader, Score> {

    @Autowired
    private ConsoleRepo consoleRepo;

    private List<Console> consoles;

    @BeforeStep
    public void getConsoles(){
        consoles = consoleRepo.findAll();
    }

    @Override
    public Score process(ScoreReader scoreReader) throws Exception {
        Score score = new Score();
        consoles.forEach(console -> {
            if(console.getName().equals(scoreReader.getConsole())){
                score.setConsole(console);
            }
        });
        
        try {
        	 score.setUserscore(Float.parseFloat(scoreReader.getUserScore()));
        }catch(NumberFormatException e) {
        	score.setUserscore(0f);
        }

        score.setName(scoreReader.getName());
        score.setMetascore(scoreReader.getMetaScore());
        score.setDate(scoreReader.getDate());
        return score;
    }
}
