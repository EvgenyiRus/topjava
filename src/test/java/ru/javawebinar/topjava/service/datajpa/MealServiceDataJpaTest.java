package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_DATAJPA)
public class MealServiceDataJpaTest extends AbstractMealServiceTest {
    @Test
    public void getWithUser() throws Exception {
        Meal meal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(meal, ADMIN_MEAL1);
        USER_MATCHER.assertMatch(meal.getUser(), ADMIN);
    }
}