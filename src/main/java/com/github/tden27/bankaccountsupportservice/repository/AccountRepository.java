package com.github.tden27.bankaccountsupportservice.repository;

import com.github.tden27.bankaccountsupportservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account save(Account bankAccount);
}
