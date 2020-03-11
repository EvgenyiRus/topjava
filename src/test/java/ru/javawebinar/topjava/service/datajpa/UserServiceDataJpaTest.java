package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals() {
        User expectedUser = service.getWithMeals(USER_ID);
        MEAL_MATCHER.assertMatch(expectedUser.getMeals(), MEALS);
        USER_MATCHER.assertMatch(expectedUser, USER);
    }
}