package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;


@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals() {
        //user_id in Meals table 100000, 100001
        User expectedUser = service.getWithMeals(100000);
        User actualUser = service.get(100000);
        USER_MATCHER.assertMatch(actualUser, expectedUser);
    }
}