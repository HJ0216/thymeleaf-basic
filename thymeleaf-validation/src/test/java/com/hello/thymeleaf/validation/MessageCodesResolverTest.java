package com.hello.thymeleaf.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

class MessageCodesResolverTest {

  MessageCodesResolver resolver = new DefaultMessageCodesResolver();

  @Test
  void messageCodesResolverObject() {
    String[] messageCodes = resolver.resolveMessageCodes("required", "item");
    for (String code : messageCodes) {
      System.out.println("code = " + code);
    }

    assertThat(messageCodes).containsExactly("required.item", "required");
  }

  @Test
  void messageCodesResolverField() {
    String[] messageCodes = resolver.resolveMessageCodes("required", "item", "itemName",
        String.class);
    for (String messageCode : messageCodes) {
      System.out.println("messageCode = " + messageCode);
    }

    assertThat(messageCodes).containsExactly(
        "required.item.itemName",
        "required.itemName",
        "required.java.lang.String",
        "required"
    );
  }
}
