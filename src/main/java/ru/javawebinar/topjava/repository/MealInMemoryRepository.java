package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealInMemoryRepository implements MealRepository {
    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final Logger log = getLogger(MealInMemoryRepository.class);
    private AtomicInteger counter;

    {
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        save(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        save(new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 100));
        save(new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        save(new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        save(new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        save(new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        save(new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Meal save(Meal meal) {
        if (meals.values().parallelStream().anyMatch(mealStream -> mealStream.getId() == meal.getId())) {
            synchronized (meal) {
                log.debug("Updating meal");
                Meal editMeal = meals.get(meal.getId());
                meals.put(meal.getId(), meal);
                log.debug("Meal {} was edit", editMeal.getDescription());
            }
        } else {
            log.debug("Adding meal");
            meal.setId(counter.getAndIncrement());
            meals.put(meal.getId(), meal);
            log.debug("Meal {} was added", meal.getDescription());
        }
        return meal;
    }

    @Override
    public synchronized void delete(int id) {
        log.debug("Deleting meal");
        Meal removeMeal = meals.get(id);
        meals.remove(id);
        log.info("Meal id={} was deleted", removeMeal.getId());
    }

    @Override
    public List<MealTo> getAll() {
        log.debug("Getting all meals");
        return MealsUtil.filteredByStreams(meals.values(), LocalTime.MIN, LocalTime.MAX, 2000);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }
}
