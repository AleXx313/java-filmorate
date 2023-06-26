package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    @ResponseBody
    public List<Director> getDirectors(){
        return directorService.getDirectors();
    }

    @GetMapping ("/{id}")
    @ResponseBody
    public Director getDirectorById(@PathVariable (value = "id") @NotNull @Positive Long id){
        return directorService.getDirectorById(id);
    }

    @PostMapping
    @ResponseBody
    public Director addDirector(@Valid @RequestBody Director director){
        return directorService.addDirector(director);
    }

    @PutMapping
    @ResponseBody
    public Director updateDirector(@Valid @RequestBody Director director){
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@NotNull @Positive Long id){
        directorService.deleteDirector(id);
    }
}
