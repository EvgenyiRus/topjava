package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.TimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.TimeUtil.isBetweenInclusive;

public class MealService implements MealRepository {
    private Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final Logger log = getLogger(MealService.class);
    private AtomicInteger counter;
    public MealService() {
        meals.put(0, new Meal(0, LocalDateTime.now(), "Meal1", 100));
        meals.put(1, new Meal(1, LocalDateTime.now().plusDays(1), "Meal2", 3000));
        counter = new AtomicInteger(meals.values().size());
    }

    @Override
    public Meal save(Meal meal) {
        if (meals.values().parallelStream().anyMatch(mealStream -> mealStream.getId() == meal.getId())) {
            log.debug("Updating meal");
            Meal editMeal = meals.get(meal.getId());
            meals.put(meal.getId(), meal);
            log.info("Meal {} was edit", editMeal.getDescription());
        } else {
            log.debug("Adding meal");
            meal.setId(counter.getAndIncrement());
            meals.put(meal.getId(), meal);
            log.info("Meal {} was added", meal.getDescription());
        }
        return meal;
    }

    @Override
    public void delete(int id) {
        log.debug("Deleting meal");
        Meal removeMeal = meals.get(id);
        meals.remove(id);
        log.info("Meal {} was deleted", removeMeal.getDescription());
    }

    @Override
    public List<MealTo> getAll() {
        log.debug("Getting all meals");
        return filteredByStreams(meals.values(), LocalTime.MIN, LocalTime.MAX, 2000);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }


    private List<MealTo> filteredByStreams(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.parallelStream().collect(Collectors.groupingBy(Meal::getDate)).values().stream().flatMap(userMeal ->
        {
            boolean excess = userMeal.parallelStream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
            return userMeal.parallelStream()
                    .filter(meal -> TimeUtil.isBetweenInclusive(meal.getTime(), startTime, endTime))
                    .map(meal -> new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
        }).collect(Collectors.toList());
    }

    private static List<MealTo> filteredByExecutor(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) throws InterruptedException, ExecutionException {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        List<Callable<Void>> tasks = new ArrayList<>();
        final List<MealTo> mealsTo = Collections.synchronizedList(new ArrayList<>());

        meals.forEach(meal -> {
            caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            if (isBetweenInclusive(meal.getTime(), startTime, endTime)) {
                tasks.add(() -> {
                    mealsTo.add(createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
                    return null;
                });
            }
        });
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.invokeAll(tasks);
        executorService.shutdown();
        return mealsTo;
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
