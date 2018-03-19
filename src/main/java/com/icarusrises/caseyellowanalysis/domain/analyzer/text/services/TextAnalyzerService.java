package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.WordIdentifier;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;

import java.util.List;
import java.util.Set;

public interface TextAnalyzerService {
    DescriptionMatch isDescriptionExist(Set<WordIdentifier> textIdentifiers, List<WordData> words) throws AnalyzerException;
    String retrieveResultFromHtml(String htmlPayload, List<String> MbpsRegex, List<String> KbpsRegex, int groupNumber) throws AnalyzerException;
}
