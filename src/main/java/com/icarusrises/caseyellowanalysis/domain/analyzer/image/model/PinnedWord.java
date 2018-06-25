package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

public class PinnedWord {

    private String description;
    private Point centralizedLocation;

    public PinnedWord() {
    }

    public PinnedWord(String description, Point centralizedLocation) {
        this.description = description;
        this.centralizedLocation = centralizedLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getCentralizedLocation() {
        return centralizedLocation;
    }

    public void setCentralizedLocation(Point centralizedLocation) {
        this.centralizedLocation = centralizedLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PinnedWord that = (PinnedWord) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return centralizedLocation != null ? centralizedLocation.equals(that.centralizedLocation) : that.centralizedLocation == null;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (centralizedLocation != null ? centralizedLocation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PinnedWord{" +
                "description='" + description + '\'' +
                ", centralizedLocation=" + centralizedLocation +
                '}';
    }
}
