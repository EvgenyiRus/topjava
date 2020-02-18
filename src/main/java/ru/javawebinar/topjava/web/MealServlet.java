package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        configurableApplicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = configurableApplicationContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        configurableApplicationContext.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("users");
        if (userId != null) {
            SecurityUtil.setId(Integer.parseInt(userId));
            request.setAttribute("meals", mealRestController.getAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
        String action = request.getParameter("action");
        if(action==null){
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            if (StringUtils.isEmpty(request.getParameter("id"))) {
                mealRestController.create(meal);
            } else {
                mealRestController.edit(meal, getId(request));
            }
            response.sendRedirect("meals");
        }
        else if(action.equals("filterDateTime")){
            String localStartDate = request.getParameter("startDate").isEmpty() ? null : request.getParameter("startDate");
            String localEndDate = request.getParameter("endDate").isEmpty() ? null : request.getParameter("endDate");
            String localStartTime = request.getParameter("startTime").isEmpty() ? null : request.getParameter("startTime");
            String localEndTime = request.getParameter("endTime").isEmpty() ? null : request.getParameter("endTime");
            request.setAttribute("meals", mealRestController.filterOnDateTime(localStartDate, localEndDate, localStartTime, localEndTime));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
