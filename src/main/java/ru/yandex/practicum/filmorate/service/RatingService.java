package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDao;

import java.util.List;

@Service
public class RatingService {

    private final RatingDao ratingDao;

    public RatingService(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public List<Rating> getAll() {
        return ratingDao.getAll();
    }

    public Rating getById(int id) {
        return ratingDao.getById(id);
    }
}
