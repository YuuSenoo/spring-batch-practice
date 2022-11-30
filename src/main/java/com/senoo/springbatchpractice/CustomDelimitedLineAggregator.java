package com.senoo.springbatchpractice;

import org.springframework.batch.item.file.transform.ExtractorLineAggregator;

public class CustomDelimitedLineAggregator<T> extends ExtractorLineAggregator<T> {

    private String delimiter = ",";

    private char quoteCharacter = '"';

    /**
     * Public setter for the delimiter.
     *
     * @param sDelimiter
     *            the delimiter to set
     */
    public void setDelimiter(String sDelimiter) {
        delimiter = sDelimiter;
    }

    public void setQuoteCharacter(char sQuoteCharacter) {
        quoteCharacter = sQuoteCharacter;
    }

    @Override
    public String doAggregate(Object[] fields) {
        StringBuilder aStringBuilder = new StringBuilder();
        int nbFields = (fields.length - 1);
        for (int i = 0; i <= nbFields; i++) {
            aStringBuilder.append(quoteCharacter).append(fields[i]).append(quoteCharacter);
            if (i < nbFields) {
                aStringBuilder.append(delimiter);
            }
        }
        return aStringBuilder.toString();
    }
}
