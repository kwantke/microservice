package org.msa.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.msa.authentication.util.JWTUtil;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    @NotBlank(message = "ID는 필수 입력 값입니다.")
    @Size(max = 10, message = "ID는 크기 10이하까지 작성가능합니다.")
    private String accountId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    private String token;

    public String writeToken(AccountDTO accountDTO) {
        return JWTUtil.generate(accountDTO);
    }
}
