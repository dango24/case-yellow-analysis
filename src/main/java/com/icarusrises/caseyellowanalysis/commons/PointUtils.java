package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.Point;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;

import java.util.List;
import java.util.function.ToIntFunction;

public interface PointUtils {

    static double calcPointDistance(Point point1, Point point2) {
        double a = point1.getX() - point2.getX();
        double b = point1.getY() - point2.getY();

        return Math.sqrt(Math.pow(a, 2) + Math.pow(b,2));
    }

    static Point getCenter(Point p1, Point p2) {
        int x = (p1.getX() + p2.getX()) /2;
        int y = (p1.getY() + p2.getY()) /2;

        return new Point(x, y);
    }

    static Point getCenter(List<Point> vertices) {
        int minX = PointUtils.getMinX(vertices);
        int minY = PointUtils.getMinY(vertices);
        int maxX = PointUtils.getMaxX(vertices);
        int maxY = PointUtils.getMaxY(vertices);

        Point center = new Point( (minX + maxX)/2, (minY + maxY)/2);

        return center;
    }

    static int getMinX(List<Point> vertices) {
        return getMin(Point::getX, vertices);
    }

    static int getMinY(List<Point> vertices) {
        return getMin(Point::getY, vertices);
    }

    static int getMaxX(List<Point> vertices) {
        return getMax(Point::getX, vertices);
    }

    static int getMaxY(List<Point> vertices) {
        return getMax(Point::getY, vertices);
    }

    static int getMin(ToIntFunction<? super Point> intMinFunction, List<Point> points) {

        return points.stream()
                     .mapToInt(intMinFunction)
                     .min()
                     .orElseThrow(() -> new AnalyzeException("There is no min point in points: " + points));
    }

    static int getMax(ToIntFunction<? super Point> intMaxFunction, List<Point> points) {

        return points.stream()
                     .mapToInt(intMaxFunction)
                     .max()
                     .orElseThrow(() -> new AnalyzeException("There is no max point in points: " + points));
    }
}
