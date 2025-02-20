package org.msa.authentication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang.time.DateUtils;
import org.msa.authentication.dto.AccountDTO;

import java.util.Date;

public class JWTUtil {
    public static String generate(AccountDTO accountDTO) {
        Date now = new Date();
        return JWT.create()
                .withSubject(accountDTO.getAccountId())
                .withExpiresAt(DateUtils.addSeconds(now, 10))
                .withIssuedAt(now)
                .sign(Algorithm.HMAC512("secretKey"));
    }
}
