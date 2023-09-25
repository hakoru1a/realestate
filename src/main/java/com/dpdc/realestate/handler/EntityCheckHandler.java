package com.dpdc.realestate.handler;

import com.dpdc.realestate.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class EntityCheckHandler {

    private static Environment env;

    @Autowired
    public EntityCheckHandler(Environment env) {
        EntityCheckHandler.env = env;
    }

    public static  <T, ID> T checkEntityExistById(CrudRepository<T, ID> repository, ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(env.getProperty("db.notify.not_found")));
    }

}