package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ;
    public static final Meal mealUser1 = new Meal(MEAL_ID+2,LocalDateTime.of(2020, 02, 23, 9, 0), "Завтрак User", 100);
    public static final Meal mealUser2 = new Meal(MEAL_ID+3,LocalDateTime.of(2020, 02, 23, 13, 0), "Обед User", 200);
    public static final Meal mealUser3 = new Meal(MEAL_ID+4,LocalDateTime.of(2020, 02, 23, 19, 0), "Ужин User", 150);
    public static final Meal mealAdmin1 = new Meal(MEAL_ID+5,LocalDateTime.of(2020, 02, 24, 9, 0), "Ужин User", 200);
    public static final Meal mealAdmin2 = new Meal(MEAL_ID+6,LocalDateTime.of(2020, 02, 24, 13, 0), "Ужин User", 300);
    public static final Meal mealAdmin3 = new Meal(MEAL_ID+7,LocalDateTime.of(2020, 02, 24, 19, 0), "Ужин User", 250);

    public static Meal getNew() {
        return new Meal(null,
                LocalDateTime.of(2020, 02, 23, 9, 0),
                "newMeal", 100);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(mealUser1);
        updated.setDescription("UpdatedDateTime");
        updated.setCalories(1000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
