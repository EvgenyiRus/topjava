package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак1", 500,1),
            new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Обед1", 1000,1),
            new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин2", 500,2),
            new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Еда на граничное значение2", 100,2),
            new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак1", 1000,1),
            new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед1", 500,1),
            new Meal(null,LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин1", 410,1)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filteredByStreams(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filteredByStreams(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenInclusive(meal.getTime(), startTime, endTime));
    }

    public static List<MealTo> filteredByStreams(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
