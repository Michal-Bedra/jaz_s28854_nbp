package pl.pjwst;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RestControllerAdvice
@RequestMapping("/currency")
@Tag(name = "Currency", description = "The Currency API")
public class CurController {

    CurService curService;

    public CurController(CurService curService) {
        this.curService = curService;
    }


    @GetMapping("/{curCode}")
    @ResponseBody()
    @Operation(summary = "Get average rate for currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Wrong currency code",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "This currency was not found",
                    content = @Content)
    })
    public ResponseEntity<Double> getCurrency(@PathVariable String curCode, @RequestParam(defaultValue = "1") Integer last) throws CurServiceException {
        return new ResponseEntity<>(curService.getAverageRate(curCode, last), HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>("Currency not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurServiceException.class)
    public ResponseEntity<String> handleCurrencyServiceException(CurServiceException e) {
        return new ResponseEntity<>("Bad request",HttpStatus.BAD_REQUEST);
    }

}

