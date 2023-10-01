package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewDao {

    Review postReview(Review review);

    Review putReview(Review review);
    void deleteReview(Review review);
    Review getReview(Review review);

    List<Review> getAllReviewByFilm(long filmId, int count);


}
