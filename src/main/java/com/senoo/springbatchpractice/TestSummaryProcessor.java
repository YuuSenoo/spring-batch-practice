package com.senoo.springbatchpractice;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TestSummaryProcessor implements ItemProcessor<TestScores, TestSummary> {

    @Override
    public TestSummary process(TestScores testScores) throws Exception {
        int totalScore = testScores.getJapaneseScore()
                + testScores.getMathScore()
                + testScores.getScienceScore()
                + testScores.getSocietyScore()
                + testScores.getEnglishScore();

        return TestSummary.builder()
                .studentId(testScores.getStudentId())
                .totalScore(totalScore)
                .build();
    }

}
