package pl.pollub.integracja_projekt.Services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pollub.integracja_projekt.Models.InterestRates;
import pl.pollub.integracja_projekt.Repositories.InterestRatesRepository;
import pl.pollub.integracja_projekt.Utils.XmlReader.InterestRatesDTO;
import pl.pollub.integracja_projekt.Utils.XmlReader.XmlReader;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class InterestRatesService {

    private final InterestRatesRepository repository;

    @Autowired
    public InterestRatesService(InterestRatesRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void loadInterestRates() {
        XmlReader readXml = new XmlReader("Data/stopy_procentowe_archiwum.xml");
        List<InterestRatesDTO> list;
        list = readXml.parseXML();

        list.forEach(interestRatesDTO -> {
            InterestRates interestRates = new InterestRates();
            interestRatesDTO.getEntryList().forEach(entry -> {
                interestRates.setDate(interestRatesDTO.getEffectiveFrom());
                switch (entry.getId()){
                    case "ref":
                        interestRates.setRefRate(Double.parseDouble(entry.getInterestPercentage()));
                        break;
                    case "lom":
                        interestRates.setPawnRate(Double.parseDouble(entry.getInterestPercentage()));
                        break;
                    case "dep":
                        interestRates.setDepRate(Double.parseDouble(entry.getInterestPercentage()));
                        break;
                    case "red":
                        interestRates.setRedRate(Double.parseDouble(entry.getInterestPercentage()));
                        break;
                    case "dys":
                        interestRates.setDisRate(Double.parseDouble(entry.getInterestPercentage()));
                        break;
                    default:
                        break;
                }
            });
            repository.save(interestRates);
        });

    }
    public InterestRates addInterestRates(InterestRates interestRates) {
        if(interestRates == null
            || interestRates.getDate() == null
            || interestRates.getRefRate() == null
            || interestRates.getPawnRate() == null
            || interestRates.getDepRate() == null
            || interestRates.getRedRate() == null
            || interestRates.getDisRate() == null
        ){
            throw new IllegalArgumentException("Data not valid");
        }
        return repository.save(interestRates);
    }

    public InterestRates deleteInterestRates(Integer id){
        InterestRates record = repository.findById(id).orElse(null);
        if(record != null){
            repository.delete(record);
            return record;
        }
        throw new IllegalArgumentException("Id not found");
    }

    public InterestRates updateInterestRates(int id, InterestRates interestRates) {
        InterestRates record = repository.findById(id).orElse(null);
        if(record == null){
            throw new NoSuchElementException("Id not found");
        }
        if(interestRates == null
            || interestRates.getDate() == null
            || interestRates.getRefRate() == null
            || interestRates.getPawnRate() == null
            || interestRates.getDepRate() == null
            || interestRates.getRedRate() == null
            || interestRates.getDisRate() == null
        ){
            throw new IllegalArgumentException("Data not valid");
        }

        record.setDate(interestRates.getDate());
        record.setRefRate(interestRates.getRefRate());
        record.setPawnRate(interestRates.getPawnRate());
        record.setDepRate(interestRates.getDepRate());
        record.setRedRate(interestRates.getRedRate());
        record.setDisRate(interestRates.getDisRate());

        return repository.save(record);
    }

    public List<InterestRates> getInterestRatesByDateRange(String fromDate, String toDate){
        if(fromDate == null || toDate == null){
            throw new IllegalArgumentException("You need to specify time period");
        }
        return repository.findByDateBetween(fromDate, toDate);
    }

    public List<InterestRates> getInterestRatesFromDate(String fromDate){
        if(fromDate == null){
            throw new IllegalArgumentException("You need to specify start date");
        }
        return repository.findByDateAfter(fromDate);
    }
}
