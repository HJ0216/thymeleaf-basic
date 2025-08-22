package com.hello.thymeleaf.domain;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Item {

  private Long id;
  private String name;
  private Integer price;
  private Integer quantity;

  private Boolean open; // 판매 여부
  private List<String> regions; // 등록 지역
  private ItemType itemType; // 상품 종류
  private String deliveryCode; // 배송 방식

  public Item(String name, Integer price, Integer quantity){
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

}
