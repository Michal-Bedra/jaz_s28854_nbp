package pl.pjwst;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import pl.pjwst.model.Result;
import pl.pjwst.model.DTO.CurrencyDTO;
import pl.pjwst.model.DTO.RateDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

class CurrencyServiceException extends Exception {
    public CurrencyServiceException(String message) {
        super(message);
    }
}

class NotFoundException extends CurrencyServiceException {
    public NotFoundException(String message) {
        super(message);
    }
}

class BadRequestException extends CurrencyServiceException {
    public BadRequestException(String message) {
        super(message);
    }
}

@Service
public class CurrencyService {
    private final RestTemplate restTemplate;
    private final CurrencyResultRepository currencyResultRepository;
    public CurrencyService(RestTemplate restTemplate, CurrencyResultRepository currencyResultRepository) {
        this.restTemplate = restTemplate;
        this.currencyResultRepository = currencyResultRepository;
    }

    public Double getAverageRate(String currencyCode, int last) throws CurrencyServiceException {
        CurrencyDTO currencyDTO;
        try {
            currencyDTO = getCurrency(currencyCode, last);
        } catch (HttpStatusCodeException e) {
            HttpStatusCode statusCode = e.getStatusCode();
            if (statusCode.equals(NOT_FOUND)) {
                throw new NotFoundException("Currency not found");
            } else if (statusCode.equals(BAD_REQUEST)) {
                throw new BadRequestException("Bad request");
            }
            throw new CurrencyServiceException("Unknown error");
        }
        double averageRate = calculateAverageRate(currencyDTO);
        saveResultToDatabase(currencyDTO.getCurrency(), currencyCode, last, averageRate, LocalDateTime.now());
        return averageRate;
    }

    private double calculateAverageRate(CurrencyDTO currencyDTO) {
        return currencyDTO.getRates().stream().mapToDouble(RateDTO::getMid).average().orElse(0);
    }

    private void saveResultToDatabase(String currency, String currencyCode, int days, double averageRate, LocalDateTime requestDate) {
        this.currencyResultRepository.save(new Result(currency, currencyCode, averageRate, days, requestDate));
    }

    private CurrencyDTO getCurrency(String currencyCode, int last) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        return restTemplate.exchange("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "/last/" + last, HttpMethod.GET, entity, CurrencyDTO.class).getBody();
    }

}
