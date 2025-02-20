package org.msa.gatewayserver.repository;

import org.msa.gatewayserver.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByAccountIdAndToken(String accountId, String token);
}
