package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        List<UserMealWithExcess> mealsToFilteredByCycles = filteredByCycles(meals, LocalTime.of(10, 0), LocalTime.of(10, 0), 2000);
        List<UserMealWithExcess> mealsToFilteredByStreams = filteredByStreams(meals, LocalTime.of(10, 0), LocalTime.of(10, 0), 2000);
    }

    private static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapWithSumCaloriesUserMealPerDay = new HashMap<>();

        for (UserMeal userMeal : meals) {
            mapWithSumCaloriesUserMealPerDay.merge(userMeal.getLocalDate(), userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenInclusive(userMeal.getLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(
                        new UserMealWithExcess(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                mapWithSumCaloriesUserMealPerDay.get(userMeal.getLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExcessList;
    }

    private static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(Collectors.groupingBy(UserMeal::getLocalDate)).values().stream().flatMap(n ->
        {
            boolean excess = n.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
            return n.stream()
                    .filter(userMeal -> TimeUtil.isBetweenInclusive(userMeal.getLocalTime(), startTime, endTime))
                    .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess));
        }).collect(Collectors.toList());
    }
}
