package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import java.util.List;

public interface MpaStorage {

    List<MpaRating> findAll();

    MpaRating getById(int id);
}
