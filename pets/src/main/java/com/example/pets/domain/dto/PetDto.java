package com.example.pets.domain.dto;

import com.example.pets.domain.Pet;
import com.example.pets.domain.User;
import com.example.pets.domain.util.MessageHelper;

public class PetDto {
    private Long id;
    private String text;
    private String tag;
    private User author;
    private String filename;
    private Long likes;
    private Boolean meLiked;

    public PetDto(Pet pet, Long likes, Boolean meLiked) {
        this.id = pet.getId();
        this.text = pet.getText();
        this.tag = pet.getTag();
        this.author = pet.getAuthor();
        this.filename = pet.getFilename();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTag() {
        return tag;
    }

    public User getAuthor() {
        return author;
    }

    public String getFilename() {
        return filename;
    }

    public Long getLikes() {
        return likes;
    }

    public Boolean getMeLiked() {
        return meLiked;
    }

    @Override
    public String toString() {
        return "PetDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", tag='" + tag + '\'' +
                ", author=" + author +
                ", filename='" + filename + '\'' +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }
}
