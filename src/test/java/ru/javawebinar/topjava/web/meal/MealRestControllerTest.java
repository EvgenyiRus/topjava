package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(MEAL1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    void save() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());
        Meal created = readFromJson(action, Meal.class);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal editMeal = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(editMeal)))
                .andExpect(status().isOk())
                .andDo(print());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), editMeal);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER_TO.contentJson(MealsUtil.getTos(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void filter() throws Exception {
        List<Meal> meals = List.of(MEAL1, MEAL2).stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        String startDateTime = MEAL1.getDateTime().toString();
        String endDateTime = MEAL2.getDateTime().plusMinutes(1).toString();
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?" +
//                .param("startDateTime",startDateTime)
//                        .param("endDateTime",endDateTime))
//                "startDateTime=2020-01-30T10:00:00.000" +
//                "&endDateTime=2020-01-30T13:01:00.000"))
                "startDateTime=" + startDateTime +
                "&endDateTime=" + endDateTime))
                //.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MEAL_MATCHER_TO.contentJson(MealsUtil.getTos(meals, USER.getCaloriesPerDay())));
    }

    @Test
    void filter2() throws Exception {
        List<Meal> meals = List.of(MEAL1, MEAL2).stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        String startDate = "";//MEAL1.getDateTime().toLocalDate().toString();
        String endDate = "";//MEAL2.getDateTime().toLocalDate().toString();
        String startTime = MEAL1.getDateTime().toLocalTime().toString();
        String endTime = MEAL2.getDateTime().toLocalTime().plusMinutes(1).toString();
        perform(MockMvcRequestBuilders.get(REST_URL + "filter2?")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("startTime", startTime)
                .param("endTime", endTime))
//                "startDateTime=2020-01-30T10:00:00.000" +
//                "&endDateTime=2020-01-30T13:01:00.000"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MEAL_MATCHER_TO.contentJson(MealsUtil.getTos(meals, USER.getCaloriesPerDay())));
    }
}