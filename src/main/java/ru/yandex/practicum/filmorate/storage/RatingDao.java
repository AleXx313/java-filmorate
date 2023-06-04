package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingDao {

    Rating getById(int id);

    List<Rating> getAll();
}
