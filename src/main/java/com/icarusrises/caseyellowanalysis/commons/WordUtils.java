package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.PinnedWord;

import java.util.function.ToIntFunction;

public interface WordUtils {

    static PinnedWord createPinnedWord(WordData wordData) {
        double x = getAverage(wordData, Point::getX);
        double y = getAverage(wordData, Point::getY);

        return new PinnedWord(wordData.getDescription(), new Point((int)x, (int)y));
    }

    static double getAverage(WordData wordData, ToIntFunction<? super Point> intFunction) {

        return wordData.getBoundingPoly()
                       .getVertices()
                       .stream()
                       .mapToInt(intFunction)
                       .average()
                       .getAsDouble();
    }

     static double euclideanDistance(Point p1, Point p2) {
        return Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }
}
