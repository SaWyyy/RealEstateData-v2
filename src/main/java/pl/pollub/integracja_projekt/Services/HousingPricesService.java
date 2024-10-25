package pl.pollub.integracja_projekt.Services;

import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pollub.integracja_projekt.Models.HousingPrices;
import pl.pollub.integracja_projekt.Repositories.HousingPricesRepository;
import pl.pollub.integracja_projekt.Utils.ExcelReader.ExcelReader;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class HousingPricesService {

    private final HousingPricesRepository repository;

    @Autowired
    public HousingPricesService(HousingPricesRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void loadHousingPrices(){
        try {
            ExcelReader reader = new ExcelReader("Data/ceny_mieszkan.xlsx");
            List<List<String>> list;
            list = reader.getArr();

            list.stream().skip(1).forEach(row -> {
                try {
                    HousingPrices housingPrices = new HousingPrices();
                    housingPrices.setName(row.get(1));
                    housingPrices.setTransaction(row.get(2));
                    housingPrices.setSurface(row.get(3));
                    housingPrices.setYear(Integer.parseInt(row.get(4)));
                    housingPrices.setPrice((int)Double.parseDouble((row.get(5).equals("-")) ? "0" : row.get(5)));

                    repository.save(housingPrices);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing row: " + row + ". Error: " + e.getMessage());
                }
            });
        } catch (RuntimeException e){
            System.err.println("Error loading housing prices: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public HousingPrices addHousingPrices(HousingPrices housingPrices) {
        if(housingPrices == null
            || housingPrices.getName() == null
            || housingPrices.getTransaction() == null
            || housingPrices.getSurface() == null
            || housingPrices.getYear() == null
            || housingPrices.getPrice() == null
        ){
            throw new IllegalArgumentException("Error adding housing prices");
        }
        return repository.save(housingPrices);
    }

    public HousingPrices deleteHousingPrices(Integer id) {
        HousingPrices record = repository.findById(id).orElse(null);
        if(record != null) {
             repository.delete(record);
             return record;
        }
        throw new IllegalArgumentException("Error deleting housing prices");
    }

    public HousingPrices updateHousingPrices(int id, HousingPrices housingPrices) {
        HousingPrices record = repository.findById(id).orElse(null);
        if(record == null){
            throw new NoSuchElementException("Error updating housing prices");
        }
        if(housingPrices == null
            || housingPrices.getName() == null
            || housingPrices.getTransaction() == null
            || housingPrices.getSurface() == null
            || housingPrices.getYear() == null
            || housingPrices.getPrice() == null
        )
        {
            throw new IllegalArgumentException("Error updating housing prices");
        }

        record.setName(housingPrices.getName());
        record.setTransaction(housingPrices.getTransaction());
        record.setSurface(housingPrices.getSurface());
        record.setYear(housingPrices.getYear());
        record.setPrice(housingPrices.getPrice());

        return repository.save(record);
    }

    public List<HousingPrices> getHousingPricesByName(String name){
        if(name == null){
            throw new IllegalArgumentException("Name must be specified");
        }
            return repository.findByName(name);
    }

    public List<HousingPrices> getHousingPricesByTransaction(String transaction){
        if(transaction == null){
            throw new IllegalArgumentException("Transaction must be specified");
        }
        return repository.findByTransaction(transaction);
    }

    public List<HousingPrices> getHousingPricesBySurface(String surface){
        if(surface == null){
            throw new IllegalArgumentException("Surface muse be specified");
        }
        return repository.findBySurface(surface);
    }

    public List<HousingPrices> getHousingPricesByYear(Integer year){
        if(year <= 1000){
            throw new IllegalArgumentException("Year cant be less than  or equal 0");
        }
        return repository.findByYear(year);
    }

    public List<HousingPrices> getHousingPricesByYearNameTransaction(Integer year, String name, String transaction){
        if(year == null || name == null || transaction == null){
            throw new IllegalArgumentException("Year, name and transaction must be specified");
        }
        return repository.findByYearAndNameAndTransaction(year, name, transaction);
    }

    public List<HousingPrices> getHousingPricesByNameTransactionSurface(String name, String transaction, String surface){
        if(name == null || transaction == null || surface == null){
            throw new IllegalArgumentException("Name, transaction and surface must be specified");
        }
        return repository.findByNameAndTransactionAndSurface(name, transaction, surface);
    }

    public List<HousingPrices> getHousingPrices(Integer year, String name, String transaction, String surface){
        if(year == null || name == null || transaction == null || surface == null){
            throw new IllegalArgumentException("Year, name, transaction and surface must be specified");
        }
        return repository.findByYearAndNameAndTransactionAndSurface(year, name, transaction, surface);
    }

    public List<HousingPrices> getHousingPricesByYearSurface(Integer year, String surface){
        if(year == null || year == null){
            throw new IllegalArgumentException("Year must be specified");
        }
        return repository.findByYearAndSurface(year, surface);
    }

    public List<HousingPrices> getHousingPricesByNameTransaction(String name, String transaction){
        if(name == null || transaction == null){
            throw new IllegalArgumentException("Name and transaction must be specified");
        }
        return repository.findByNameAndTransaction(name, transaction);
    }

    public List<HousingPrices> importData(JSONArray jsonArray) throws JSONException {
        System.out.println(jsonArray);
        repository.deleteAll();
        List<HousingPrices> list = new ArrayList<HousingPrices>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject housingPrice = jsonArray.getJSONObject(i);

            Integer id = housingPrice.getInt("id");
            String name = housingPrice.getString("name");
            String transaction = housingPrice.getString("transaction");
            String surface = housingPrice.getString("surface");
            Integer year = housingPrice.getInt("year");
            Integer price = housingPrice.getInt("price");
            HousingPrices housing = new HousingPrices(id, name, transaction, surface, year, price);
            list.add(housing);
        }
        repository.saveAll(list);
        return list;
    }
}
