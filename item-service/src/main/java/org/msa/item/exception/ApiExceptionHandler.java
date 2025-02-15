package org.msa.item.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.msa.item.dto.ResponseDTO;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// Spring에서 전역적으로 Exception에 대한 처리를 진행해주는 클래스로 지정해주는 어노테이션이다.
// 또한 @ResponseBody와 같은 특정 객체를 json 형으로 응답값을 전달하기 위해서 사용한다.
@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> Exception(HttpServletRequest request, Exception e) {
    ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
    responseBuilder.code("500").message(e.getMessage());
    return ResponseEntity.ok(responseBuilder.build());
  }

  // 유효성 체크 후 에러가 발생하면, MethodArgumentNotValidException 예외 에러 내용이 출력된다.
  // responseBuilder.code("500").message(errorMessage); 원래는 message에 trace 가 전체 출력되었던 내용을
  // 이제 json 데이터 응답값으로 전달하도록 구현한다.
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    /*StringBuilder builder = new StringBuilder();
    int i = 0;
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      builder.append("[");
      builder.append(fieldError.getField());
      builder.append("](은)는 ");
      builder.append(fieldError.getDefaultMessage());
      builder.append(" 입력된 값: [");
      builder.append(fieldError.getRejectedValue());
      builder.append("]");
      i++;
      if (i<bindingResult.getFieldErrors().size()) {
        builder.append(", ");
      }

    }*/

    String errorMessage = bindingResult.getFieldErrors().stream()
            .map(fieldError -> String.format("[%s](은)는 %s 입력된 값: [%s]",
                      fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()))
            .collect(Collectors.joining(", "));

    ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
    responseBuilder.code("500").message(errorMessage);
    return ResponseEntity.ok(responseBuilder.build());
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<?> apiException(HttpServletRequest request, ApiException e) {
    ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
    responseBuilder.code("501").message(e.getMessage());
    return ResponseEntity.ok(responseBuilder.build());
  }
}
