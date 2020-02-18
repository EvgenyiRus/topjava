package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    @Autowired
    private InMemoryMealRepository inMemoryMealRepository;

    public List<Meal> getAll(Integer userId) {
        return inMemoryMealRepository.getAll(userId);
    }

    public Meal create(Meal meal, int userId) {
        return inMemoryMealRepository.save(meal, userId);
    }

    public void delete(int id,int userId) {
        checkNotFoundWithId(inMemoryMealRepository.delete(id,userId), id);
    }

    public Meal get(int id,int userId) {
        return checkNotFoundWithId(inMemoryMealRepository.get(id,userId), id);
    }

    public void edit(Meal meal, int userId) {
        checkNotFoundWithId(inMemoryMealRepository.save(meal,userId), meal.getId());
    }

    public List<Meal> filterOnDateTime(String startDate, String endDate, String startTime, String endTime, int userId){
        return inMemoryMealRepository.filterOnDateTime(startDate,endDate,startTime,endTime,userId);
    }
}