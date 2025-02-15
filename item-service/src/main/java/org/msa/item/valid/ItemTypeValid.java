package org.msa.item.valid;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.msa.item.dto.constant.ItemType;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

@Constraint(validatedBy = ItemTypeValid.ItemTypeValidator.class) // 유효성 체크할때 어떤 valid클래스로 처리할지 지정하는 것
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER}) //이 어노테이션이 어디에 사용할수 있는지 지정(함수, 필드, 파라미터)
@Retention(RetentionPolicy.RUNTIME) // 어느시점까지 어노테이션 메모리를 갖고 갈 것인지 지정
public @interface ItemTypeValid {
  String message() default "허용되지 않은 물품 유형입니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  class ItemTypeValidator implements ConstraintValidator<ItemTypeValid, String> {

    @Override
    public boolean isValid(String cd, ConstraintValidatorContext context) {
      boolean hasItemType = false;
      List<ItemType> itemTypeList = Arrays.asList(ItemType.values());
      hasItemType = itemTypeList.stream().anyMatch(item -> item.hasItemCd(cd));
      return hasItemType;
    }
  }
}

