package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Review {
    private long id;
    private String content;
    private boolean isPositive;
    private long userId;
    private long filmId;
    private int useFull;

    public void changeUseFullness(boolean isGood){
        if(isGood){
            useFull++;
        } else {
            useFull--;
        }
    }
}
