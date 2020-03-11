package ru.javawebinar.topjava.service.jpa;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import java.util.concurrent.TimeUnit;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_JPA)
public class MealServiceJpaTest extends AbstractMealServiceTest {
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
                "\nMealServiceJpaTest    Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    public static StringBuilder getLog() {
        return resultsFinish;
    }
}
