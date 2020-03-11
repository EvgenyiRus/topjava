package ru.javawebinar.topjava.service.datajpa;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {
    private static StringBuilder results = new StringBuilder();
    private static StringBuilder resultsFinish = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
        }
    };

    @AfterClass
    public static void printLog() {
        resultsFinish.append("\n---------------------------------" +
                "\nUserServiceDataJpaTest    Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    public static StringBuilder getLog() {
        return resultsFinish;
    }

    @Test
    public void getWithMeals() {
        User expectedUser = service.getWithMeals(USER_ID);
        MEAL_MATCHER.assertMatch(expectedUser.getMeals(), MEALS);
        USER_MATCHER.assertMatch(expectedUser, USER);
    }
}