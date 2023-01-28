package com.example.pets.service;

import com.example.pets.domain.User;
import com.example.pets.domain.dto.PetDto;
import com.example.pets.repos.PetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private PetRepo petRepo;

    @Autowired
    public MessageService(PetRepo petRepo) {
        this.petRepo = petRepo;
    }

    public Page<PetDto> messageList(String filter, Pageable pageable, User user) {
        if (filter != null && !filter.isEmpty())
            return petRepo.findByTag(filter, pageable, user);
        else
            return petRepo.findAll(pageable, user);
    }

    public Page<PetDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return petRepo.findByUser(pageable, author,currentUser);
    }
}
