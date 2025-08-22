package com.hello.thymeleaf.repository;

import com.hello.thymeleaf.domain.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

  private static final Map<Long, Item> store = new HashMap<>();
  private static long sequence = 0L;

  public List<Item> findAll() {
    return new ArrayList<>(store.values());
  }

  public Item findById(Long id) {
    return store.get(id);
  }

  public Item save(Item item) {
    item.setId(++sequence); // Repository가 persistence 책임을 가지므로
    store.put(item.getId(), item);
    return item;
  }

  public void update(Long itemId, Item updateParam) {
    Item findItem = findById(itemId);
    findItem.setName(updateParam.getName());
    findItem.setPrice(updateParam.getPrice());
    findItem.setQuantity(updateParam.getQuantity());
  }
}
