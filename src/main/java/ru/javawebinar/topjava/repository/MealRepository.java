package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal,Integer userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id,int userId);

    List<Meal> getAll(int userId);

    //List<Meal> filterOnDateTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int userId);
    List<Meal> filterOnDateTime(String startDate, String endDate, String startTime, String endTime, int userId);

    List<Meal> find();

}
