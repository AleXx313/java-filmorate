package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterFirstMovie;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @Size(min = 0, max = 200)
    private String description;
    @AfterFirstMovie
    private LocalDate releaseDate;
    @Positive
    private long duration;
}
