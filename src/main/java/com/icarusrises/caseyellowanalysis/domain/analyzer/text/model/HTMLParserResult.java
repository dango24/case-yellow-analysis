package com.icarusrises.caseyellowanalysis.domain.analyzer.text.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HTMLParserResult {

    private String result;
    private boolean succeed;

    public HTMLParserResult(String result) {
        this(result, true);
    }

    public static HTMLParserResult failure() {
        return new HTMLParserResult(null, false);
    }
}
