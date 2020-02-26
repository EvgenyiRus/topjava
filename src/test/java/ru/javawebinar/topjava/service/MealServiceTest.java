package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void save() {
        Meal expected = MealTestData.getNew();
        Meal actual= service.create(expected,ADMIN_ID);
        assertEquals(actual,expected);
    }

    @Test
    public void update() {
        Meal expected = service.get(mealAdmin1.getId(),ADMIN_ID);
        expected.setDescription("UpdatedDescription");
        expected.setCalories(10000);
        service.update(expected,ADMIN_ID);
        Meal actual =service.get(expected.getId(),ADMIN_ID);
        assertEquals(actual,expected);
    }

    @Test(expected = NotFoundException.class)
    public void get() {
        service.get(mealUser1.getId(),ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(mealUser1.getId(),ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(mealUser1,ADMIN_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> expected= Stream.of(mealAdmin1,mealAdmin2,mealAdmin3).sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        List<Meal> actual=service.getBetweenHalfOpen(LocalDate.of(2020, 02, 24),LocalDate.of(2020, 02, 24),ADMIN_ID);
        assertEquals(actual,expected);
    }

    @Test
    public void getAll() {
        List<Meal> expected=Stream.of(mealAdmin1,mealAdmin2,mealAdmin3).sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        assertEquals(service.getAll(ADMIN_ID),expected);
    }

}