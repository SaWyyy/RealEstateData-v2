package pl.pollub.integracja_projekt.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.integracja_projekt.Models.InterestRates;
import pl.pollub.integracja_projekt.Repositories.InterestRatesRepository;
import pl.pollub.integracja_projekt.Services.InterestRatesService;

import java.util.List;

@RestController
@RequestMapping("/api/interestRates")
@AllArgsConstructor
public class InterestRatesController {

    public final InterestRatesRepository repository;
    public final InterestRatesService service;

    // localhost:8080/?fromDate=2020-03-12

    @GetMapping("/")
    ResponseEntity<List<InterestRates>> getAllRates(@RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate){
        if(fromDate != null && toDate != null){
            return ResponseEntity.ok().body(service.getInterestRatesByDateRange(fromDate, toDate));
        }
        if(fromDate != null){
            return ResponseEntity.ok(service.getInterestRatesFromDate(fromDate));
        }
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/")
    ResponseEntity<InterestRates> addInterestRates(@RequestBody InterestRates interestRates){
        try{
            return ResponseEntity.ok().body(service.addInterestRates(interestRates));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<InterestRates> deleteInterestRates(@PathVariable int id){
        try{
            return ResponseEntity.ok().body(service.deleteInterestRates(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<InterestRates> updateInterestRates(@PathVariable int id, @RequestBody InterestRates interestRates){
        try{
            return ResponseEntity.ok().body(service.updateInterestRates(id, interestRates));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
