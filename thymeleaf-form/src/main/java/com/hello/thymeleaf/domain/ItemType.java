package com.hello.thymeleaf.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemType {

  BOOK("도서"), FOOD("음식"), ETC("기타");

  private final String description;
}
