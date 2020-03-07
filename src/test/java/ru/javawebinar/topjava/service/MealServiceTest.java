package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static StringBuilder stringBuilder = new StringBuilder("\n\n=====Summary of test execution=====\n");

    @AfterClass
    public static void printLog() {
        log.info(stringBuilder.toString());
    }

//    @Rule
//    public RuleServiceTest ruleServiceTest=new RuleServiceTest();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            stringBuilder.append(String.format("Test %s was completed for %d ms\n", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos)));
            log.info(String.format("Test %s was completed for %d ms\n", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos)));
        }
    };

//    @Rule
//    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private MealService service;
    @Autowired
    private MealRepository repository;

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        Assert.assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                service.delete(1, USER_ID);
            }
        });
    }

    @Test
    public void deleteNotOwn() {
        Assert.assertThrows(NotFoundException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                service.delete(MEAL1_ID, ADMIN_ID);
            }
        });
//        exception.expect(NotFoundException.class);
//        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        Assert.assertThrows(NotFoundException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                service.get(1, USER_ID);
            }
        });
//        exception.expect(NotFoundException.class);
//        service.get(1, USER_ID);
    }

    @Test
    public void getNotOwn() throws Exception {
        Assert.assertThrows(NotFoundException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                service.get(MEAL1_ID, ADMIN_ID);
            }
        });
//        exception.expect(NotFoundException.class);
//        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        Assert.assertThrows(NotFoundException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                service.update(MEAL1, ADMIN_ID);
            }
        });
//        exception.expect(NotFoundException.class);
//        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> m = service.getAll(USER_ID);
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}