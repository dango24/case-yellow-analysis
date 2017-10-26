package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.images.model.WordResult;

import java.util.function.ToIntFunction;

public interface WordUtils {

    static WordResult createWordResult(WordData wordData) {
        double x = getAverage(wordData, Point::getX);
        double y = getAverage(wordData, Point::getY);

        return new WordResult(wordData.getDescription().replaceAll("O|o", ""), new Point((int)x, (int)y));
    }

    static double getAverage(WordData wordData, ToIntFunction<? super Point> intFunction) {

        return wordData.getBoundingPoly()
                       .getVertices()
                       .stream()
                       .mapToInt(intFunction)
                       .average()
                       .getAsDouble();
    }
}
