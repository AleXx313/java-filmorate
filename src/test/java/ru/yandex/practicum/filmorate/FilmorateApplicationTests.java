package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    private final UserService userService;
    private final FilmService filmService;
    private final GenreService genreService;
    private final RatingService ratingService;
    private User user1;
    private User user2;
    private User user3;
    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    public void init() {

        user1 = User.builder()
                .login("User1")
                .email("user1@yandex.ru")
                .name("User1Name")
                .birthday(LocalDate.of(1991, 3, 29))
                .build();
        user2 = User.builder()
                .login("User2")
                .email("user2@yandex.ru")
                .name("User2Name")
                .birthday(LocalDate.of(1992, 4, 29))
                .build();
        user3 = User.builder()
                .login("User3")
                .email("user3@yandex.ru")
                .name("User3Name")
                .birthday(LocalDate.of(1993, 5, 29))
                .build();

        film1 = Film.builder()
                .name("Film1")
                .description("Film1 description")
                .duration(100)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpa(Rating.builder().id(1).build())
                .build();
        film2 = Film.builder()
                .name("Film2")
                .description("Film2 description")
                .duration(110)
                .releaseDate(LocalDate.of(2002, 3, 10))
                .mpa(Rating.builder().id(2).build())
                .build();
        film3 = Film.builder()
                .name("Film3")
                .description("Film3 description")
                .duration(120)
                .releaseDate(LocalDate.of(2004, 5, 20))
                .mpa(Rating.builder().id(4).build())
                .build();
    }

    @Test
    public void shouldAutoFillGenresAndRatings() {
        assertEquals(genreService.getAll().size(), 6);
        assertEquals(genreService.getById(1).getName(), "Comedy");
        assertEquals(genreService.getAll().get(0).getId(), 1);
        assertEquals(ratingService.getAll().size(), 5);
        assertEquals(ratingService.getById(1).getName(), "G");
        assertEquals(ratingService.getAll().get(4).getId(), 5);
    }

    @Test
    public void shouldCreateUserGiveUserIdAndGetUserByThisIdAndUpdateUserAndDeleteUser() {
        User expectedUser = userService.create(user1);
        long expectedUserId = expectedUser.getId();
        assertTrue(expectedUserId != 0);
        User actualUser = userService.getUser(expectedUserId);
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getLogin(), actualUser.getLogin());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getBirthday(), actualUser.getBirthday());
        User updatedUser = expectedUser.toBuilder().name("UpdatedName").build();
        userService.update(updatedUser);
        assertEquals(userService.getUser(expectedUserId).getName(), "UpdatedName");
        userService.delete(expectedUserId);
        assertEquals(userService.findAll().size(), 0);
    }

    @Test
    public void shouldAddFriendsAndGetFriendsAndGetCommonFriendsAndDeleteFriends() {
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.addFriends(1, 2);
        userService.addFriends(1, 3);
        userService.addFriends(2, 3);
        assertEquals(userService.getFriends(1).size(), 2);
        assertEquals(userService.getCommonFriends(1, 2).size(), 1);
        assertEquals(userService.getFriends(1).get(0).getName(), "User2Name");
        userService.deleteFriends(1, 2);
        assertEquals(userService.getFriends(1).size(), 1);
        assertEquals(userService.getFriends(1).get(0).getName(), "User3Name");
    }

    @Test
    public void shouldCreateFilmGiveFilmIdAndGetFilmByThisIdAndUpdateFilmAndDeleteFilm() {
        Film expectedFilm = filmService.create(film1);
        long expectedFilmId = expectedFilm.getId();
        assertTrue(expectedFilmId != 0);
        Film actualFilm = filmService.getFilm(expectedFilmId);
        assertEquals(expectedFilm.getName(), actualFilm.getName());
        assertEquals(expectedFilm.getDescription(), actualFilm.getDescription());
        assertEquals(expectedFilm.getDuration(), actualFilm.getDuration());
        assertEquals(expectedFilm.getReleaseDate(), actualFilm.getReleaseDate());
        assertEquals(expectedFilm.getMpa().getName(), actualFilm.getMpa().getName());
        Film updatedFilm = expectedFilm.toBuilder().name("UpdatedFilm").build();
        filmService.update(updatedFilm);
        assertEquals(filmService.getFilm(expectedFilmId).getName(), "UpdatedFilm");
        filmService.deleteFilm(expectedFilmId);
        assertEquals(filmService.findAll().size(), 0);
    }

    @Test
    public void shouldAddGenresToFilmShouldRemoveGenreFromFilmShouldGetGenres() {
        filmService.create(film1);
        genreService.addFilmGenre(1, 1);
        genreService.addFilmGenre(1, 2);
        genreService.addFilmGenre(1, 3);
        genreService.addFilmGenre(1, 4);
        genreService.addFilmGenre(1, 5);
        genreService.addFilmGenre(1, 6);
        assertEquals(genreService.getByFilm(1).size(), 6);
        genreService.removeAllByFilm(1);
        assertEquals(genreService.getByFilm(1).size(), 0);
    }

    @Test
    public void shouldSetLikesAndGetFilmsByLikedAndGetMostPopularFilm() {
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        Film createdFilm1 = filmService.create(film1);
        Film createdFilm2 = filmService.create(film2);
        Film createdFilm3 = filmService.create(film3);
        filmService.setLikes(1, 1);
        filmService.setLikes(1, 2);
        filmService.setLikes(1, 3);
        filmService.setLikes(2, 1);
        filmService.setLikes(2, 2);
        filmService.setLikes(3, 1);
        assertEquals(filmService.getMostPopular(1), List.of(createdFilm1));
        assertEquals(filmService.getMostPopular(3), List.of(createdFilm1, createdFilm2, createdFilm3));
        filmService.removeLikes(2, 1);
        filmService.removeLikes(2, 2);
        assertEquals(filmService.getMostPopular(3), List.of(createdFilm1, createdFilm3, createdFilm2));
    }
}
