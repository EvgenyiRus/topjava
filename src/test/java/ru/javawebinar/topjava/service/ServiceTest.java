package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.service.datajpa.MealServiceDataJpaTest;
import ru.javawebinar.topjava.service.datajpa.UserServiceDataJpaTest;
import ru.javawebinar.topjava.service.jdbc.MealServiceJdbcTest;
import ru.javawebinar.topjava.service.jdbc.UserServiceJdbcTest;
import ru.javawebinar.topjava.service.jpa.MealServiceJpaTest;
import ru.javawebinar.topjava.service.jpa.UserServiceJpaTest;

import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class ServiceTest {
    private static final Logger log = getLogger("result");

    //    private static StringBuilder results = new StringBuilder();
//
//    @Rule
//    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
//    public Stopwatch stopwatch = new Stopwatch() {
//        @Override
//        protected void finished(long nanos, Description description) {
//            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
//            results.append(result);
//            log.info(result + " ms\n");
//        }
//    };
//
    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest for User entity" +
                UserServiceJpaTest.getLog() +
                UserServiceJdbcTest.getLog() +
                UserServiceDataJpaTest.getLog() +
                "\n---------------------------------" +
                "\nTest for Meal entity" +
                MealServiceJpaTest.getLog() +
                MealServiceJdbcTest.getLog() +
                MealServiceDataJpaTest.getLog() +
                "\n---------------------------------");
    }
}
