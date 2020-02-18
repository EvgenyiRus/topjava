package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        save(new User(null, "Admin", "Admin@mail.ru", "pass", Role.ROLE_ADMIN));
        save(new User(null, "User1", "User1@mail.ru", "pass", Role.ROLE_USER));
        save(new User(null, "User2", "User2@mail.ru", "pass", Role.ROLE_USER));
        save(new User(null, "User3", "User3@mail.ru", "pass", Role.ROLE_USER));
        save(new User(null, "User4", "User4@mail.ru", "pass", Role.ROLE_USER));
        save(new User(null, "User5", "User5@mail.ru", "pass", Role.ROLE_USER));
    }

    public InMemoryUserRepository() {
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id)!=null;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream().sorted(Comparator.comparing(User::getName).thenComparing(User::getId)).collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values().stream().filter(user->user.getEmail().equals(email)).findFirst().orElse(null);
    }
}
