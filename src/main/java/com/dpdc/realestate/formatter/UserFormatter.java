package com.dpdc.realestate.formatter;

import com.dpdc.realestate.models.entity.User;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class UserFormatter implements Formatter<User> {
    @Override
    public User parse(String text, Locale locale) throws ParseException {
        return new User(Integer.valueOf(text));
    }

    @Override
    public String print(User object, Locale locale) {
        return String.valueOf(object.getId());
    }
}
