package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Override
    Meal save(Meal meal);

    @Query("SELECT m FROM Meal m WHERE m.id=:id and m.user.id=:user_id")
    Meal get(@Param("id") int id, @Param("user_id") int user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    int delete(@Param("id") int id, @Param("user_id") int user_id);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime desc")
    List<Meal> findAll(@Param("userId") int userId);

    @Query("SELECT m FROM Meal m " +
            "WHERE m.dateTime>=:startDateTime and m.dateTime<:endDateTime and m.user.id=:userId " +
            "ORDER BY m.dateTime desc ")
    List<Meal> getBetweenHalfOpen(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=:id and m.user.id=:user_id")
    Meal getWithUser(@Param("id") int id, @Param("user_id") int user_id);
}
