package com.example.pets.domain.util;

import com.example.pets.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author){
        return author!=null? author.getUsername() : "<none>";
    }
}
