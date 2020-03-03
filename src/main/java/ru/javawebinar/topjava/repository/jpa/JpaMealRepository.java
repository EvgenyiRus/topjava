package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        User userRef = em.getReference(User.class, userId);
        meal.setUser(userRef);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else if (get(meal.getId(), userId) != null) {
            return em.merge(meal);
        } else return null;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE).
                setParameter("id", id).
                setParameter("user_id", userId).
                executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createNamedQuery(Meal.GET, Meal.class).
                setParameter("user_id", userId).
                setParameter("id", id).
                getResultList();
        return meals.size() > 0 ? DataAccessUtils.requiredSingleResult(meals) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GET_ALL_BY_USER, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.GET_BETWEEN_DATES, Meal.class)
                .setParameter("user_id", userId)
                .setParameter("startDateTime", startDate)
                .setParameter("endDateTime", endDate)
                .getResultList();
    }
}