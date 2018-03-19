package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.commons.PointUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionLocation;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.SpeedTestNonFlashMetaData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.WordIdentifier;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.services.central.CentralService;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.icarusrises.caseyellowanalysis.commons.PointUtils.calcPointDistance;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.*;

@Slf4j
@Service
public class TextAnalyzerServiceImp implements TextAnalyzerService {

    private OcrService ocrService;
    private CentralService centralService;

    @Autowired
    public TextAnalyzerServiceImp(CentralService centralService, OcrService ocrService) {
        this.ocrService = ocrService;
        this.centralService = centralService;
    }

    @Override
    public String retrieveResultFromHtml(String identifier, String htmlPayload) {
        SpeedTestNonFlashMetaData speedTestNonFlashMetaData = centralService.getSpeedTestNonFlashMetaData(identifier);

        return retrieveResultFromHtml(htmlPayload, speedTestNonFlashMetaData.getFinishTextIdentifier(), speedTestNonFlashMetaData.getFinishIdentifierKbps(), 1);
    }

    private String retrieveResultFromHtml(String htmlPayload, List<String> MbpsRegex, List<String> KbpsRegex, int groupNumber) throws AnalyzeException {

        boolean MbpsMatcher = verifyPatterns(MbpsRegex, htmlPayload);
        boolean KbpsMatcher = verifyPatterns(KbpsRegex, htmlPayload);

        if (MbpsMatcher && KbpsMatcher) {
            throw new AnalyzeException("Failure to find finish test identifier, Found Kbps and Mbps matchers");

        } else if (MbpsMatcher) {
            return retrieveLastMatcher(MbpsRegex, htmlPayload,groupNumber); // regex matcher result

        } else if (KbpsMatcher) {
            return convertKbpsToMbps(retrieveLastMatcher(KbpsRegex, htmlPayload, groupNumber));
        }

        return "FAILED";
    }

    @Override
    public DescriptionMatch isDescriptionExist(String identifier, boolean startTest, GoogleVisionRequest visionRequest) throws AnalyzeException {
        try {
            OcrResponse ocrResponse = ocrService.parseImage(visionRequest);
            Set<WordIdentifier> textIdentifiers = centralService.getTextIdentifiers(identifier, startTest);

            return buildDescriptionMatch(textIdentifiers, ocrResponse.getTextAnnotations());

        } catch (Exception e) {
            throw new AnalyzeException(String.format("Failed to find description in image, %s", e.getMessage()), e);
        }
    }

    private boolean verifyPatterns(List<String> regexes, String payload) {
        if (isNull(regexes) || regexes.isEmpty()) {
            return false;
        }

        return regexes.stream()
                      .filter(regex -> !StringUtils.isEmpty(regex))
                      .map(Pattern::compile)
                      .map(pattern -> pattern.matcher(payload))
                      .allMatch(Matcher::find);
    }


    private String retrieveLastMatcher(List<String> regex, String payload, int groupNumber) {
        if (isNull(regex) || regex.isEmpty() || StringUtils.isEmpty(regex.get(regex.size() -1))) {
            return null;
        }

        Pattern pattern = Pattern.compile(regex.get(regex.size() -1));
        Matcher matcher = pattern.matcher(payload);

        if (matcher.find()) {
            return matcher.group(groupNumber); // regex matcher result
        }

        return null;
    }

    private String convertKbpsToMbps(String KbpsResultStr) {
        if (StringUtils.isEmpty(KbpsResultStr)) {
            return null;
        }

        double KbpsResult = Double.valueOf(KbpsResultStr) / 1_000.0;
        return String.valueOf(KbpsResult);
    }

    private DescriptionMatch buildDescriptionMatch(Set<WordIdentifier> textIdentifiers, List<WordData> words) {
        Map<WordData, Long> matchWordsInText;
        Map<String, List<Point>> wordDescription;

        if (textIdentifiers.isEmpty() || words.isEmpty()) {
            return DescriptionMatch.notFound();
        } else if (textIdentifiers.size() > 2) {
            throw new AnalyzeException("Not supported more than two words for matching description");
        }

        matchWordsInText = findMatchingWords(textIdentifiers, words);

        if (matchWordsInText.isEmpty()) {
            return DescriptionMatch.notFound();
        } else if (isSingleIdentifier(textIdentifiers, matchWordsInText)) {
            return getSingleMatchDescription(matchWordsInText);
        }

        wordDescription = buildMatchingWordsDescription(matchWordsInText);

        if (!matchIdentifiersInText(textIdentifiers, matchWordsInText)) {
            log.error("Not found text identifiers in text, textIdentifiers: " + textIdentifiers + ", matchWordsInText: " + matchWordsInText);
            return DescriptionMatch.notFound();
        }

        String description = wordDescription.keySet().stream().collect(joining(" "));
        Point center = buildCenterMatchingPoint(wordDescription);

        return new DescriptionMatch(description, center);
    }

    private boolean matchIdentifiersInText(Set<WordIdentifier> textIdentifiers, Map<WordData, Long> wordDescription) {
        int numOfWordsIdentifiers = getNumOfWordsIdentifiers(textIdentifiers);
        int numOfWordsFoundInText = getNumOfWordsFoundInText(wordDescription);

        if (numOfWordsIdentifiers != numOfWordsFoundInText) {
            return false;
        }

        Set<String> wordsNotInText = buildWordsNotFoundInText(textIdentifiers);

        Map<String, Integer> wordsInText = buildWordsFoundInText(textIdentifiers);

        boolean wordsNotFoundInText = isWordsNotFoundInText(wordDescription, wordsNotInText);
        boolean wordsFoundInText = isWordsFoundInText(wordDescription, wordsInText);

        return wordsFoundInText && wordsNotFoundInText;
    }

    private int getNumOfWordsFoundInText(Map<WordData, Long> wordDescription) {

        return wordDescription.values()
                              .stream()
                              .mapToInt(Long::intValue)
                              .sum();
    }

    private int getNumOfWordsIdentifiers(Set<WordIdentifier> textIdentifiers) {

        return textIdentifiers.stream()
                              .filter(wordIdentifier -> wordIdentifier.getCount() != -1)
                              .mapToInt(WordIdentifier::getCount)
                              .sum();
    }

    private  Map<String, Integer> buildWordsFoundInText(Set<WordIdentifier> textIdentifiers) {

        return textIdentifiers.stream()
                              .filter(wordIdentifier -> wordIdentifier.getCount() != -1)
                              .collect(toMap(WordIdentifier::getIdentifier, WordIdentifier::getCount));
    }

    private Set<String> buildWordsNotFoundInText(Set<WordIdentifier> textIdentifiers) {

        return textIdentifiers.stream()
                              .filter(wordIdentifier -> wordIdentifier.getCount() == -1)
                              .map(WordIdentifier::getIdentifier)
                              .collect(toSet());
    }

    private boolean isWordsNotFoundInText(Map<WordData, Long> wordDescription, Set<String> wordsNotInText) {

        return wordDescription.keySet()
                              .stream()
                              .map(WordData::getDescription)
                              .noneMatch(word -> wordsNotInText.contains(word));
    }

    private boolean isWordsFoundInText(Map<WordData, Long> wordDescription, Map<String, Integer> wordsInText) {

        return wordDescription.entrySet()
                              .stream()
                              .allMatch(wordEntry -> wordEntry.getValue() == (int)wordsInText.get(wordEntry.getKey().getDescription()));
    }

    private Point buildCenterMatchingPoint(Map<String, List<Point>> wordDescription) {

        double distance;
        double minDistance = Integer.MAX_VALUE;
        Point firstPoint = null;
        Point secondPoint = null;
        List<List<Point>> points = new ArrayList<>(wordDescription.values());
        List<Point> firstPointsList = points.get(0);

        if (wordDescription.size() == 1) {
            return firstPointsList.get(0);
        }

        List<Point> secondPointsList = points.get(1);

        for (Point point1 : firstPointsList) {
            for (Point point2 : secondPointsList) {
                distance = calcPointDistance(point1, point2);

                if (distance < minDistance) {
                    firstPoint = point1;
                    secondPoint = point2;
                    minDistance = distance;
                }
            }
        }

        return PointUtils.getCenter(firstPoint, secondPoint);
    }

    private Map<String, List<Point>> buildMatchingWordsDescription(Map<WordData, Long> matchWordsInText) {

        return matchWordsInText.keySet()
                               .stream()
                               .map(this::convertWordDataToDescriptionLocation)
                               .collect(groupingBy(DescriptionLocation::getDescription, mapping(DescriptionLocation::getCenter, Collectors.toList())));
    }

    private DescriptionMatch getSingleMatchDescription(Map<WordData, Long> matchWordsInText) {
        WordData wordData = matchWordsInText.keySet().iterator().next();
        return new DescriptionMatch(wordData.getDescription(), getCenter(wordData));
    }

    private boolean isSingleIdentifier(Set<WordIdentifier> textIdentifiers, Map<WordData, Long> matchWordsInText) {
        WordIdentifier wordIdentifier = textIdentifiers.iterator().next();
        Long matchWordCount = matchWordsInText.get(new WordData(wordIdentifier.getIdentifier()));

        return matchWordsInText.size() == 1 &&
               textIdentifiers.size() == 1 &&
               wordIdentifier.getCount() != -1 &&
               Objects.nonNull(matchWordCount) &&
               wordIdentifier.getCount() == matchWordCount;
    }

    private Map<WordData, Long> findMatchingWords(Set<WordIdentifier> textIdentifiers, List<WordData> words) {
        Set<String> wordsIdentifiers = textIdentifiers.stream()
                                                      .map(WordIdentifier::getIdentifier)
                                                      .collect(toSet());
        return words.stream()
                    .filter(word -> wordsIdentifiers.contains(word.getDescription()))
                    .collect(groupingBy(Function.identity(), Collectors.counting()));
    }

    private DescriptionLocation convertWordDataToDescriptionLocation(WordData wordData) {
        return new DescriptionLocation(wordData.getDescription(), getCenter(wordData));
    }

    private Point getCenter(WordData wordData) {
        return PointUtils.getCenter(wordData.getBoundingPoly().getVertices());
    }
}
