package ru.javawebinar.topjava.service.datajpa;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_DATAJPA)
public class MealServiceDataJpaTest extends AbstractMealServiceTest {
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
                "\nMealServiceDataJpaTest    Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    public static StringBuilder getLog() {
        return resultsFinish;
    }

    @Test
    public void getWithUser() throws Exception {
        Meal meal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(meal, ADMIN_MEAL1);
        USER_MATCHER.assertMatch(meal.getUser(), ADMIN);
    }

    @Test
    public void getNotFound() throws Exception {
        Assert.assertEquals(service.getAll(1).size(), 0);
    }
}