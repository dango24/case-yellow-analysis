package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.commons.PointUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.*;
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

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.getImageResolution;
import static com.icarusrises.caseyellowanalysis.commons.PointUtils.calcPointDistance;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
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
    public HTMLParserResult retrieveResultFromHtml(String identifier, HTMLParserRequest htmlParserRequest) {
        SpeedTestNonFlashMetaData speedTestNonFlashMetaData = centralService.getSpeedTestNonFlashMetaData(identifier);

        return retrieveResultFromHtml(speedTestNonFlashMetaData, htmlParserRequest.getPayload(), 1);
    }

    private HTMLParserResult retrieveResultFromHtml(SpeedTestNonFlashMetaData speedTestNonFlashMetaData, String htmlPayload, int groupNumber) throws AnalyzeException {

        List<String> MbpsRegex = speedTestNonFlashMetaData.getFinishIdentifierMbps();
        List<String> KbpsRegex = speedTestNonFlashMetaData.getFinishIdentifierKbps();
        boolean MbpsMatcher = verifyPatterns(MbpsRegex, htmlPayload);
        boolean KbpsMatcher = verifyPatterns(KbpsRegex, htmlPayload);

        if (MbpsMatcher && KbpsMatcher) {
            throw new AnalyzeException("Failure to find finish test identifier, Found Kbps and Mbps matchers");

        } else if (MbpsMatcher) {
            return retrieveLastMatcher(speedTestNonFlashMetaData, htmlPayload,groupNumber); // regex matcher result

        } else if (KbpsMatcher) {
            return convertKbpsToMbps(retrieveLastMatcher(speedTestNonFlashMetaData, htmlPayload, groupNumber));
        }

        return HTMLParserResult.failure();
    }

    @Override
    public DescriptionMatch isDescriptionExist(String identifier, boolean startTest, GoogleVisionRequest visionRequest) throws AnalyzeException {
        try {
            OcrResponse ocrResponse = ocrService.parseImage(visionRequest);
            Set<WordIdentifier> textIdentifiers = centralService.getTextIdentifiers(identifier, startTest);
            String imageResolution = getImageResolution(visionRequest.getRequests().get(0));

            return buildDescriptionMatch(textIdentifiers, ocrResponse.getTextAnnotations(), imageResolution);

        } catch (Exception e) {
            throw new AnalyzeException(String.format("Failed to find description exist in image, %s", e.getMessage()), e);
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


    private HTMLParserResult retrieveLastMatcher(SpeedTestNonFlashMetaData speedTestNonFlashMetaData, String payload, int groupNumber) {
        if (!validateRegex(speedTestNonFlashMetaData)) {
            return HTMLParserResult.failure();
        }

        String result =
                retrieveResultFromPayload(speedTestNonFlashMetaData.getRetrieveResultFromPayloadFloat(), payload, groupNumber);

        if (isNull(result)) {
            result =
                retrieveResultFromPayload(speedTestNonFlashMetaData.getRetrieveResultFromPayloadInteger(), payload, groupNumber);
        }

        if (nonNull(result)) {
            return new HTMLParserResult(result);
        } else {
            return HTMLParserResult.failure();
        }
    }

    private String retrieveResultFromPayload(String regex, String payload, int groupNumber) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(payload);

        if (matcher.find()) {
            return matcher.group(groupNumber); // regex matcher result
        }

        return null;
    }

    private boolean validateRegex(SpeedTestNonFlashMetaData speedTestNonFlashMetaData) {
        List<String> regex = Arrays.asList(speedTestNonFlashMetaData.getRetrieveResultFromPayloadFloat(), speedTestNonFlashMetaData.getRetrieveResultFromPayloadInteger());

        if (isNull(regex) || regex.isEmpty() || regex.stream().anyMatch(StringUtils::isEmpty)) {
            return false;
        }

        return true;
    }

    private HTMLParserResult convertKbpsToMbps(HTMLParserResult htmlParserResult) {
        if (isNull(htmlParserResult) || StringUtils.isEmpty(htmlParserResult.getResult())) {
            return HTMLParserResult.failure();
        }

        String KbpsResultStr = htmlParserResult.getResult();
        double KbpsResult = Double.valueOf(KbpsResultStr) / 1_000.0;

        return new HTMLParserResult(String.valueOf(KbpsResult));
    }

    private DescriptionMatch buildDescriptionMatch(Set<WordIdentifier> textIdentifiers, List<WordData> words, String imageResolution) {
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
