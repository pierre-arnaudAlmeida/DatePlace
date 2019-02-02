package fr.blackmamba.dateplaceapp;

public class ville {

    private String name;
    private String distance;
    private String like;

    public ville (String name, String distance,  String like) {
        this.distance = distance;
        this.name = name;
        this.like = like;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}

