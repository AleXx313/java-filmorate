package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    private int id;
    @NotBlank(message = "Email не может быть null!")
    @Email(message = "Введенная строка не является email!")
    private String email;
    @NotBlank(message = "Логин не может быть пустым!")
    private final String login;
    private String name;
    @Past(message = "День рождения не может быть в будущем!")
    private LocalDate birthday;
}
