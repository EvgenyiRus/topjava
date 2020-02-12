package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {
    private MealService mealService = new MealService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestParameterAction = getUrlParameter(request, "action");
        if (requestParameterAction == null) {
            request.setAttribute("mealList", mealService.getAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (requestParameterAction.equals("delete")) {
            String id = request.getParameter("id");
            mealService.delete(Integer.valueOf(id));
            response.sendRedirect("meals");
        } else if (requestParameterAction.equals("edit")) {
            String id = request.getParameter("id");
            Meal meal = mealService.get(Integer.parseInt(id));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String localDateTime = request.getParameter("dateTime-local");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");
        Meal meal = new Meal(
                id.isEmpty() ? null : Integer.parseInt(id),
                localDateTime.isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(localDateTime),
                description.isEmpty() ? "Unnamed meal" : description,
                calories.isEmpty() ? Integer.parseInt("0") : Integer.parseInt(calories));
        mealService.save(meal);
        request.setAttribute("mealList", mealService.getAll());
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private String getUrlParameter(HttpServletRequest request, String parameter) {
        return request.getParameter(parameter);
    }
}
