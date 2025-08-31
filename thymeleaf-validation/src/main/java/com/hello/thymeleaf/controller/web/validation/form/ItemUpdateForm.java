package com.hello.thymeleaf.controller.web.validation.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemUpdateForm {

  @NotNull
  private Long id;

  @NotBlank
  private String itemName;

  @NotNull
  @Range(min = 1_000, max = 1_000_000)
  private Integer price;

  private Integer quantity;

}
