package ru.javawebinar.topjava.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/*Данный класс создавался, чтобы выполнить последний пункт из optional,
 * но никак не получилось реализовать вывод в лог в конце класса сводки по всем методам */
public class RuleServiceTest implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(RuleServiceTest.class);

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = System.currentTimeMillis();
                statement.evaluate();
                long endTime = System.currentTimeMillis();
                log.info(String.format("\n\nTest %s was completed for %d ms\n", description.getMethodName(), endTime-startTime));
            }
        };
    }
}
