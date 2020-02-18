package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MealService mealService;

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("get all meals for user {}", userId);
        return MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create meal {}", meal);
        checkNew(meal);
        return mealService.create(meal, userId);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        return mealService.get(id, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
    }

    public void edit(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        mealService.edit(meal, userId);
    }

    public List<MealTo> filterOnDateTime(String startDate, String endDate, String startTime, String endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("Filter on date or time meal list for user {}", userId);
        return MealsUtil.getTos(mealService.filterOnDateTime(startDate, endDate, startTime, endTime, userId), SecurityUtil.authUserCaloriesPerDay());
    }
}