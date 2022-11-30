package com.senoo.springbatchpractice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestScores implements Serializable {

    private String studentId;

    private int japaneseScore;
    private int mathScore;
    private int scienceScore;
    private int societyScore;
    private int englishScore;

}
