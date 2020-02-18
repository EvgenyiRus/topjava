package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (getUserMeals(id, userId).size() == 0)
            return false;
        repository.remove(id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        if (getUserMeals(id, userId).size() == 0)
            return null;
        return repository.get(id);
    }

    private List<Meal> getUserMeals(int id, int userId) {
        return repository.values().stream().
                filter(userMeal -> userMeal.getUserId() == userId && userMeal.getId() == id).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream().
                filter(meal -> meal.getUserId() == userId).
                sorted(Comparator.comparing(Meal::getDateTime).reversed()).
                collect(Collectors.toList());
    }

    @Override
    public List<Meal> filterOnDateTime(String startDate, String endDate, String startTime, String endTime, int userId) {
        if (startDate != null && endDate != null)
            return getAll(userId).stream().
                    filter(meal ->DateTimeUtil.isBetweenInclusive(meal.getDate(), LocalDate.parse(startDate), LocalDate.parse(endDate))).
                    collect(Collectors.toList());
        else if (startTime != null && endTime != null) {
            return getAll(userId).stream().
                    filter(meal ->DateTimeUtil.isBetweenInclusive(meal.getTime(), LocalTime.parse(startTime), LocalTime.parse(endTime))).
                    collect(Collectors.toList());
        }
        return getAll(userId);
    }

    @Override
    public List<Meal> find() {
        return null;
    }
}

