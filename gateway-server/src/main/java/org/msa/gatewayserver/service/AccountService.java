package org.msa.gatewayserver.service;

import lombok.RequiredArgsConstructor;
import org.msa.gatewayserver.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    // 토근과 사용자ID를 통해 row 여부 조회
    public boolean existsByAccountIdAndToken(String accountId, String token) {
        return accountRepository.existsByAccountIdAndToken(accountId, token);
    }
}
