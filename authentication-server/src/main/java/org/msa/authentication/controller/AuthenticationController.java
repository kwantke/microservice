package org.msa.authentication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.authentication.domain.Account;
import org.msa.authentication.dto.AccountDTO;
import org.msa.authentication.dto.ResponseDTO;
import org.msa.authentication.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/account")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AccountService accountService;

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public ResponseEntity<ResponseDTO> join(@Valid @RequestBody AccountDTO accountDTO) {
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
        Account account = accountService.selectAccount(accountDTO);

        if (account != null) {
            responseBuilder.code("100").message("already join user");
        } else {
            accountService.saveAccount(accountDTO, null);
            responseBuilder.code("200").message("success");
        }

        log.debug("join.account.id = {}", accountDTO.getAccountId());
        return ResponseEntity.ok(responseBuilder.build());
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<ResponseDTO> token(@Valid @RequestBody AccountDTO accountDTO) {
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
        Account account = accountService.selectAccount(accountDTO);

        if (account == null) {
            responseBuilder.code("101").message("Unknown user.");
        } else {
            String token = accountDTO.writeToken(accountDTO);
            accountService.saveAccount(accountDTO, token);
            responseBuilder.code("200").message("success");
            responseBuilder.token(token);
        }

        log.debug("token.account.id = {}", accountDTO.getAccountId());
        return ResponseEntity.ok(responseBuilder.build());
    }

    private String getToken(AccountDTO accountDTO) {
        return null;
    }




















}
