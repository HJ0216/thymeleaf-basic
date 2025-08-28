package com.hello.thymeleaf.controller.web.validation;

import com.hello.thymeleaf.domain.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Item.class.isAssignableFrom(clazz);
    // 자식 class까지 검증 가능
  }

  @Override
  public void validate(Object target, Errors errors) {
    Item item = (Item) target;

    if (!StringUtils.hasText(item.getItemName())) {
      errors.rejectValue("itemName", "required");
    }
    if (item.getPrice() == null || item.getPrice() < 1_000 || item.getPrice() > 1_000_000) {
      errors.rejectValue("price", "range", new Object[]{1_000, 1_000_000}, null);
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9_999) {
      errors.rejectValue("quantity", "max", new Object[]{9_999}, null);
    }

    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10_000) {
        // object의 필드가 아닐 경우,
        errors.reject("totalPriceMin", new Object[]{10_000, resultPrice}, null);
      }
    }
  }
}
