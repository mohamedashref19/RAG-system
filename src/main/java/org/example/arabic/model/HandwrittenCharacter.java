package org.example.arabic.model;

public class HandwrittenCharacter {
    private String character;
    private String imagePath;

    public HandwrittenCharacter(String character, String imagePath) {
        this.character = character;
        this.imagePath = imagePath;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "HandwrittenCharacter{" +
                "character='" + character + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}

