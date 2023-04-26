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
    @NotBlank(message = "Название фильма не может быть пустым!")
    private final String name;
    @Size(min = 0, max = 200, message = "Описание фильма не должно превышать 200 символов!")
    private String description;
    @AfterFirstMovie(message = "Дата релиза указана неверно!")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма не может быть меньше или равна нулю!")
    private long duration;
}
