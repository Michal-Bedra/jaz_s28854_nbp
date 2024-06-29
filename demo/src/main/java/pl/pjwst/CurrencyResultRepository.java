package pl.pjwst;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjwst.model.Result;

public interface CurrencyResultRepository extends JpaRepository<Result, Long> {

}
