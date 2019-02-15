package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.model;

public class Game {
    private int id;
    private String name;
    private String genre;
    private String description;
    private String company;
    private int price;
    private byte[] img;

    public Game(int id, String name, String genre, String description, String company, int price, byte[] img) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.description = description;
        this.company = company;
        this.price = price;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
