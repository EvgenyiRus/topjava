package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.formatter.DateFormatter;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    public static final String REST_URL = "/rest/meals";

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @GetMapping("/filter")
    public List<MealTo> filter(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime
            , @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return super.getBetween(startDateTime.toLocalDate(), startDateTime.toLocalTime(), endDateTime.toLocalDate(), endDateTime.toLocalTime());
    }

    @GetMapping("/filter2")
    public List<MealTo> filter2(
            @RequestParam(value = "startDate",required = false) LocalDate startDate
            , @RequestParam(value = "startTime",required = false) LocalTime startTime
            , @RequestParam(value = "endDate",required = false) LocalDate endDate
            , @RequestParam(value = "endTime",required = false) LocalTime endTime) {
        return super.getBetween(startDate == null ? LocalDate.MIN: startDate//LocalDate.EPOCH : startDate
                , startTime == null ? LocalTime.MIN : startTime
                , endDate == null ? LocalDate.now().plusDays(1) : endDate
                , endTime == null ? LocalTime.MAX : endTime);
    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Meal create(@RequestBody Meal meal){
//        Meal newMeal=super.create(meal);
//        return newMeal;
//    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> create2(@RequestBody Meal meal) {
        Meal newMeal = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(newMeal.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newMeal);
    }

    //@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping("/{id}")
    public Meal update(@PathVariable int id, @RequestBody Meal meal) {
        super.update(meal, id);
        return super.get(id);
    }

}