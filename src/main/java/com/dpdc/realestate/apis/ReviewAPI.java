package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.models.entity.Review;
import com.dpdc.realestate.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews/")
public class ReviewAPI {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private Environment env;

    @GetMapping("/{staffId}/")
    public ResponseEntity<ModelResponse> getReviews(@RequestParam(defaultValue = "1") String page,
                                                    @PathVariable Integer staffId ) throws NotFoundException {
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1,
                Integer.parseInt(env.getProperty("app.page.size")));
        Page<Review> reviews = reviewService.getReviews(staffId,pageable);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), reviews),
                HttpStatus.OK);
    }


    @PostMapping("/{staffId}")
    public ResponseEntity<ModelResponse> addReview(@RequestBody @Valid Review review,
             @PathVariable Integer staffId
            , BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        try{
            Review savedReview = reviewService.addReview(staffId, review);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedReview),
                    HttpStatus.OK);
        }
        catch (NotFoundException | DataAlreadyExistException e){
            throw  e;
        }
        catch (Exception exception){
            throw new Exception(env.getProperty("db.notify.save_fail"));
        }
    }

    @DeleteMapping("/{staffId}/")
    public ResponseEntity<ModelResponse> deleteReviews(
            @RequestBody Map<String, Set<Integer>> reviewIds,
            @PathVariable Integer staffId ) throws Exception {
        Set<Integer> ids = reviewIds.getOrDefault("reviewIds", new HashSet<>());
        try{
            reviewService.deleteReviews(staffId, ids);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), null),
                    HttpStatus.NO_CONTENT);
        }
        catch (NotFoundException  notFoundException){
            throw notFoundException;
        }
        catch (Exception e){
            throw new Exception(env.getProperty("db.notify.delete_fail"));
        }
    }
}
