package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.ExceptionInfoHandler;

@Component
public class UserEmailValidator implements Validator {

    @Autowired
    private UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        //Создаем интерфейс HasEmail с методом получения Email, далее подключаем его в User and UserTo
        //RestController принимает User, UIController принимает UserTo
        HasEmail user = (HasEmail) object;
        User validUser = repository.getByEmail(user.getEmail().toLowerCase());
        if (validUser != null && !(validUser.getId().equals(user.getId()))) {
            errors.rejectValue("email", ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL);
        }
    }
}
