package com.example.pets.controller;

import com.example.pets.domain.Pet;
import com.example.pets.domain.User;
import com.example.pets.domain.dto.PetDto;
import com.example.pets.repos.PetRepo;
import com.example.pets.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class PetController {
    @Value("${upload.path}")
    private String uploadPath;

    private MessageService messageService;

    private PetRepo petRepo;

    @Autowired
    public PetController(PetRepo petRepo, MessageService messageService) {
        this.petRepo = petRepo;
        this.messageService = messageService;
    }


    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @AuthenticationPrincipal User user) {

        Page<PetDto> page = messageService.messageList(filter, pageable,user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);
        return "main";
    }


    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @Valid Pet pet, BindingResult bindingResult,
                      Model model,
                      @RequestParam("file") MultipartFile file,
                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) throws IOException {
        pet.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("pet", pet);
        } else {
            saveFile(pet, file);
            model.addAttribute("pet", null);
            petRepo.save(pet);
        }
        model.addAttribute("url", "/main");
        Page<PetDto> page = this.messageService.messageList("", pageable,user); // пустой фильтр
        model.addAttribute("page", page);

        return "main";

    }

    private void saveFile(Pet pet, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) uploadDir.mkdir();

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            pet.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-pets/{author}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User author,
                               Model model,
                               @RequestParam(required = false) Pet pet,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PetDto> page = messageService.messageListForUser(pageable, currentUser, author);
        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("page", page);
        model.addAttribute("pet", pet);
        model.addAttribute("isCurrentUser", author.equals(currentUser));
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("url", "/user-pets/" + author.getId());

        return "userPets";
    }

    @PostMapping("/user-pets/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long user,
                                @RequestParam("pet") Pet pet,
                                @RequestParam("text") String text,
                                @RequestParam("tag") String tag,
                                @RequestParam("file") MultipartFile file) throws IOException {
        if (pet.getAuthor().equals(currentUser)) {

            if (!text.isEmpty()) {
                pet.setText(text);
            }
            if (!tag.isEmpty()) {
                pet.setTag(tag);
            }
            saveFile(pet, file);
            petRepo.save(pet);
        }


        return "redirect:/user-pets/" + user;
    }

    @GetMapping("/pets/{pet}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Pet pet,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> likes = pet.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
}
