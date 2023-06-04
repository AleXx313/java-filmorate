package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.h2.tools.RunScript;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateApplicationTests {
	private final UserService userService;
	private final FilmService filmService;
	private final FriendService friendService;
	private final GenreService genreService;
	private final LikesService likesService;
	private final RatingService ratingService;
	private User user1;
	private User user2;
	private User user3;
	private Film film1;
	private Film film2;
	private Film film3;

	@BeforeEach
	public void init(){

		user1 = User.builder()
				.login("User1")
				.email("user1@yandex.ru")
				.name("User1Name")
				.birthday(LocalDate.of(1991, 3,29))
				.build();
		user2 = User.builder()
				.login("User2")
				.email("user2@yandex.ru")
				.name("User2Name")
				.birthday(LocalDate.of(1992, 4,29))
				.build();
		user3 = User.builder()
				.login("User3")
				.email("user3@yandex.ru")
				.name("User3Name")
				.birthday(LocalDate.of(1993, 5,29))
				.build();

		film1 = Film.builder()
				.name("Film1")
				.description("Film1 description")
				.duration(100)
				.releaseDate(LocalDate.of(2000,1,1))
				.mpa(Rating.builder().id(1).build())
				.build();
		film2 = Film.builder()
				.name("Film2")
				.description("Film2 description")
				.duration(110)
				.releaseDate(LocalDate.of(2002,3,10))
				.mpa(Rating.builder().id(2).build())
				.build();
		film3 = Film.builder()
				.name("Film3")
				.description("Film3 description")
				.duration(120)
				.releaseDate(LocalDate.of(2004,5,20))
				.mpa(Rating.builder().id(4).build())
				.build();
	}

	@Test
	public void shouldAutoFillGenresAndRatings(){
		assertEquals(genreService.getAll().size(), 6);
		assertEquals(genreService.getById(1).getName(), "Комедия");
		assertEquals(genreService.getAll().get(0).getId(), 1);
		assertEquals(ratingService.getAll().size(), 5);
		assertEquals(ratingService.getById(1).getName(), "G");
		assertEquals(ratingService.getAll().get(4).getId(), 5);
	}
	@Test
	public void shouldCreateUserGiveUserIdAndGetUserByThisId(){
		User expectedUser = userService.create(user1);
		long expectedUserId = expectedUser.getId();
		assertTrue(expectedUserId != 0);
		User actualUser = userService.getUser(expectedUserId);
		assertEquals(expectedUser.getName(), actualUser.getName());
		assertEquals(expectedUser.getLogin(), actualUser.getLogin());
		assertEquals(expectedUser.getEmail(), actualUser.getEmail());
		assertEquals(expectedUser.getBirthday(), actualUser.getBirthday());
	}
	@Test
	public void shouldCreateFilmGiveFilmIdAndGetFilmByThisId(){
		Film expectedFilm = filmService.create(film1);
		long expectedFilmId = expectedFilm.getId();
		assertTrue(expectedFilmId != 0);
		Film actualFilm = filmService.getFilm(expectedFilmId);
		assertEquals(expectedFilm.getName(), actualFilm.getName());
		assertEquals(expectedFilm.getDescription(), actualFilm.getDescription());
		assertEquals(expectedFilm.getDuration(), actualFilm.getDuration());
		assertEquals(expectedFilm.getReleaseDate(), actualFilm.getReleaseDate());
		assertEquals(expectedFilm.getMpa().getName(), actualFilm.getMpa().getName());
	}
	@Test
	public void shouldAddFriendsAndGetFriendsAndCommonFriends(){
		User createdUser1 = userService.create(user1);// id = 1
		User createdUser2 = userService.create(user2);// id = 2
		User createdUser3 = userService.create(user3);// id = 3

		userService.addFriends(1, 2);


	}




}
