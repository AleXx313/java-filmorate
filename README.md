# java-filmorate
Template repository for Filmorate project.

## Описание
Проект предсталвяет собой простую реализаю бекэнда веб приложения по поиску фильмов и оценке фильмов для рекомендации их своим друзьям.

## Обучение
В рамках данного проекта я:
- чуть глубже похзникомился со Spring Framework
- научился сохранять данные в базе данных с помощью JdbcTemplate
- познакомился с валидацией данных в spring
- познакомился с понятиями Data Transfer Object и Data Access Layout
- узнал ряд принципов проектирования приложения

<details>
<summary>База данных проекта</summary>

### Диаграмма базы данных (https://dbdiagram.io/d/6467bcc8dca9fb07c469a085)
![Диаграмма базы данных](https://github.com/AleXx313/java-filmorate/blob/main/diagrama.png)
### Список SELECT запросов к Базе Данных
<details>
<summary>Получить список всех пользователей</summary>

>SELECT *<br>
FROM users;
</details>
<details>
<summary>Получить пользователя по ID</summary>

>SELECT *<br>
>FROM users<br>
>WHERE id = {id};
</details>
<details>
<summary>Получить список имен друзей пользователя</summary>

>SELECT u.name<br>
>FROM friends AS f<br>
>JOIN users AS u on f.friend_id = u.id<br>
>WHERE f.user_id = {id} AND f.status_id = 1;
</details>
<details>
<summary>Получить список общих друзей</summary>

>SELECT c.name<br>
>FROM (SELECT * <br>
>       FROM friends AS f <br>
>       JOIN users AS u ON f.friend_id = u.id <br>
>       WHERE f.status_id = 1<br> AND (f.user_id = 2 or f.user_id = 1)<br>
) AS c<br>
> GROUP BY c.name<br>
> HAVING COUNT(c.friend_id) = 2;
</details>
<details>
<summary>Получить список фильмов</summary>

>SELECT *<br>
>FROM films;
</details>
<details>
<summary>Получить фильм по ID</summary>

>SELECT *<br>
>FROM films<br>
>WHERE id = {id};
</details>
<details>
<summary>Получить список самых популярных фильмов</summary>

>SELECT f.name, COUNT (l.user_id) = u.id<br>
>FROM films AS f<br>
>LEFT OUTER JOIN likes AS l on f.id = l.film_id<br>
>GROUP BY f.name<br>
>ORDER by likes DESC<br>
>LIMIT 5;
</details>
</details>
