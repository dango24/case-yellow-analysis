package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WordData {

    private int index;
    private String description;
    private BoundingPoly boundingPoly;

    public WordData() {
    }

    public WordData(WordData wordData) {
        this.index = wordData.index;
        this.description = wordData.description;
        this.boundingPoly = wordData.boundingPoly;
    }

    public WordData(WordData wordData, int index) {
        this(wordData);
        this.index = index;
    }

    public WordData(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BoundingPoly getBoundingPoly() {
        return boundingPoly;
    }

    public void setBoundingPoly(BoundingPoly boundingPoly) {
        this.boundingPoly = boundingPoly;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordData wordData = (WordData) o;

        return description != null ? description.equals(wordData.description) : wordData.description == null;
    }

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }
}
