package pl.pjwst;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

class CurServiceException extends Exception {
    public CurServiceException(String message) {
        super(message);
    }
}

class NotFoundException extends CurServiceException {
    public NotFoundException(String message) {
        super(message);
    }
}

class BadRequestException extends CurServiceException {
    public BadRequestException(String message) {
        super(message);
    }
}

@Service
public class CurService {
    private final RestTemplate restTemplate;
    private final CurResultRepository curResultRepository;
    public CurService(RestTemplate restTemplate, CurResultRepository curResultRepository) {
        this.restTemplate = restTemplate;
        this.curResultRepository = curResultRepository;
    }

    public Double getAverageRate(String currencyCode, int last) throws CurServiceException {
        Currency currency;
        try {
            currency = getCurrency(currencyCode, last);
        } catch (HttpStatusCodeException e) {
            HttpStatusCode statusCode = e.getStatusCode();
            if (statusCode.equals(NOT_FOUND)) {
                throw new NotFoundException("Currency not found");
            } else if (statusCode.equals(BAD_REQUEST)) {
                throw new BadRequestException("Bad request");
            }
            throw new CurServiceException("Unknown error");
        }
        double averageRate = calculateAverageRate(currency);
        saveResultToDatabase(currency.getCurrency(), currencyCode, last, averageRate, LocalDateTime.now());
        return averageRate;
    }

    private double calculateAverageRate(Currency currency) {
        return currency.getRates().stream().mapToDouble(Rate::getMid).average().orElse(0);
    }

    private void saveResultToDatabase(String currency, String currencyCode, int days, double averageRate, LocalDateTime requestDate) {
        this.curResultRepository.save(new Result(currency, currencyCode, averageRate, days, requestDate));
    }

    private Currency getCurrency(String currencyCode, int last) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        return restTemplate.exchange("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode + "/last/" + last, HttpMethod.GET, entity, Currency.class).getBody();
    }

}
