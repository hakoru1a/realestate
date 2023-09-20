package com.dpdc.realestate.formatter;

import com.dpdc.realestate.models.entity.Customer;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class CustomerFormatter implements Formatter<Customer> {
    @Override
    public Customer parse(String text, Locale locale) throws ParseException {
        return new Customer(Integer.valueOf(text));

    }
    @Override
    public String print(Customer object, Locale locale) {
        return String.valueOf(object.getId());

    }
}
