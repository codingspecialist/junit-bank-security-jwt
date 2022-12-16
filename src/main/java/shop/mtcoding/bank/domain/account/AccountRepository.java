package shop.mtcoding.bank.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user u WHERE ac.number = :number")
    Optional<Account> findByNumber(@Param("number") Long number);
}
