package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorDao;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDao;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorDao directorDao;
    private final FilmDao filmDao;

    public DirectorService(DirectorDao directorDao, @Qualifier("filmDaoImpl")  FilmDao filmDao) {
        this.directorDao = directorDao;
        this.filmDao = filmDao;
    }

    public List<Director> getDirectors(){
        return directorDao.getDirectors();
    }

    public Director getDirectorById(long id){
        return directorDao.getDirectorById(id);
    }

    public Director addDirector(Director director){
        return directorDao.addDirector(director);
    }

    public Director updateDirector (Director director){
        return directorDao.updateDirector(director);
    }

    public void deleteDirector (long id){
        directorDao.deleteDirector(id);
    }
}
