package com.hello.thymeleaf.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

  @Autowired
  MessageSource source;

  @Test
  void helloMessage(){
    String result = source.getMessage("hello", null, null);
    assertThat(result).isEqualTo("안녕");
  }

  @Test
  void notFoundMessageCode(){
    assertThatThrownBy(() -> source.getMessage("no_code", null, null))
        .isInstanceOf(NoSuchMessageException.class);
  }

  @Test
  void notFoundMessageCodeDefaultMessage(){
    String result = source.getMessage("no_coed", null, "default message", null);
    assertThat(result).isEqualTo("default message");
  }

  @Test
  void argumentMessage(){
    String result = source.getMessage("hello.name", new Object[]{"Spring"}, null);
    assertThat(result).isEqualTo("안녕, Spring!");
  }

  @Test
  void defaultLang(){
    assertThat(source.getMessage("hello", null, null)).isEqualTo("안녕");
    assertThat(source.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
  }

  @Test
  void enLang(){
    assertThat(source.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
  }

}
