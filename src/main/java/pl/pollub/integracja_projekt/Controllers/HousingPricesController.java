package pl.pollub.integracja_projekt.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pollub.integracja_projekt.Models.HousingPrices;
import pl.pollub.integracja_projekt.Repositories.HousingPricesRepository;
import pl.pollub.integracja_projekt.Services.HousingPricesService;

import java.util.List;

@RestController
@RequestMapping("/api/housingPrices")
@AllArgsConstructor
public class HousingPricesController {

    private final HousingPricesService service;

    @PostMapping("/")
    ResponseEntity<HousingPrices> create(@RequestBody HousingPrices housingPrices) {
        try{
            return ResponseEntity.ok().body(service.addHousingPrices(housingPrices));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/")
    ResponseEntity<List<HousingPrices>> getHousingPrices(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "transaction", required = false) String transaction, @RequestParam(value = "surface", required = false) String surface){
        try{
            if(year == null && surface == null && transaction == null)
                return ResponseEntity.ok().body(service.getHousingPricesByName(name));

            if(year == null && surface == null && name == null)
                return ResponseEntity.ok().body(service.getHousingPricesByTransaction(transaction));

            if(year == null && transaction == null && name == null)
                return ResponseEntity.ok().body(service.getHousingPricesBySurface(surface));

            if(surface == null && transaction == null && name == null)
                return ResponseEntity.ok().body(service.getHousingPricesByYear(year));

            if(transaction == null && name == null)
                return ResponseEntity.ok().body(service.getHousingPricesByYearSurface(year, surface));

            if(year == null && surface == null)
                return ResponseEntity.ok().body(service.getHousingPricesByNameTransaction(name, transaction));

            if(surface == null)
                return ResponseEntity.ok().body(service.getHousingPricesByYearNameTransaction(year, name, transaction));

            if(year == null)
                return  ResponseEntity.ok().body(service.getHousingPricesByNameTransactionSurface(name, transaction, surface));


            return ResponseEntity.ok(service.getHousingPrices(year, name, transaction, surface));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @DeleteMapping("/{id}")
    ResponseEntity<HousingPrices> delete(@PathVariable Integer id){
        try{
            return ResponseEntity.ok().body(service.deleteHousingPrices(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<HousingPrices> update(@PathVariable Integer id, @RequestBody HousingPrices housingPrices){
        try{
           return ResponseEntity.ok().body(service.updateHousingPrices(id, housingPrices));
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/import")
    ResponseEntity<List<HousingPrices>> importToDb(JSONArray jsonArray){
        try{
            return ResponseEntity.ok().body(service.importData(jsonArray));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
