package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals() {
        //user_id in Meals table 100000, 100001
        User expectedUser = service.getWithMeals(USER_ID);
        User actualUser = service.get(USER_ID);
        Assert.assertEquals(expectedUser.getMeals().size(), MEALS.size());
        USER_MATCHER.assertMatch(actualUser, expectedUser);
    }
}