package com.dpdc.realestate.formatter;

import com.dpdc.realestate.models.entity.Property;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class PropertyFormatter implements Formatter<Property> {
    @Override
    public Property parse(String text, Locale locale) throws ParseException {
        return new Property(Integer.valueOf(text));
    }

    @Override
    public String print(Property object, Locale locale) {
        return String.valueOf(object.getId());
    }
}
