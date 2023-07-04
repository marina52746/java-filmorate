package ru.yandex.practicum.filmorate.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaRating> findAll() {
        return mpaStorage.findAll();
    }

    public MpaRating getById(int id) {
        return mpaStorage.getById(id);
    }
}
