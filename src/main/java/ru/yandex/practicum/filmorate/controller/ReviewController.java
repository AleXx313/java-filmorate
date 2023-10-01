package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;

@RestController
@Slf4j
@RequestMapping("/reviews")
public class ReviewController {

    @PostMapping
    public Review postReview(@RequestBody Review review){
        return review;
    }

   // @
}
