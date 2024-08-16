package ru.kata.spring.boot_security.demo.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Optional;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        Optional<User> optionalPerson = Optional.ofNullable(userService.findByUsername(user.getUsername()));

        if (optionalPerson.isPresent()) errors.rejectValue("username", "", "Человек с таким именем пользователя существует");
    }
    public String getErrorMsg(BindingResult bindingResult){
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        errors.forEach(error -> errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";"));
        return errorMsg.toString();
    }
}
