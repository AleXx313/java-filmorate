# Проект java-filmorate
### Диаграмма базы данных проекта (https://dbdiagram.io/d/6467bcc8dca9fb07c469a085)
![Image alt](https://github.com/AleXx313/java-filmorate/blob/main/Database%20schema.png)

### Select запросы для тестирования:
**Получить список пользователей**  
SELECT * FROM users   
**Получить пользователя с Логином - {Логин}**  
SELECT * FROM users WHERE login = '{Логин}'  
**Получить список фильмов**  
SELECT * FROM films  
**Получить фильм с названием - {Название фильма}**  
SELECT * FROM films WHERE name = '{Название фильма}'
