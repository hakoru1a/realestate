package com.dpdc.realestate.formatter;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Payment;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class PaymentFormatter implements Formatter<Payment> {
    @Override
    public Payment parse(String text, Locale locale) throws ParseException {
        return new Payment(Integer.valueOf(text));

    }
    @Override
    public String print(Payment object, Locale locale) {
        return String.valueOf(object.getId());

    }
}