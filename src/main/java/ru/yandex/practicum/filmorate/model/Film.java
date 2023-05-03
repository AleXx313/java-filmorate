package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.AfterFirstMovie;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Film {
    private int id;
    @Builder.Default
    private final Set<Integer> likes = new HashSet<>();
    @NotBlank(message = "Название фильма не может быть пустым!")
    private String name;
    @Size(min = 0, max = 200, message = "Описание фильма не должно превышать 200 символов!")
    private String description;
    @AfterFirstMovie(message = "Дата релиза указана неверно!")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма не может быть меньше или равна нулю!")
    private long duration;
}
