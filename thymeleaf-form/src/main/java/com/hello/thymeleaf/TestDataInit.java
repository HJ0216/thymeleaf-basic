package com.hello.thymeleaf;

import com.hello.thymeleaf.domain.Item;
import com.hello.thymeleaf.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

  private final ItemRepository itemRepository;

  @PostConstruct
  public void init() {
    itemRepository.save(new Item("itemA", 10000, 10));
    itemRepository.save(new Item("itemB", 20000, 20));
  }

  // 1. 스프링 컨테이너 시작
  // 2. 해당 클래스의 빈 생성
  // 3. 의존성 주입 완료
  // 4. @PostConstruct 메서드 자동 실행: init() 호출
  // 5. 빈 사용 준비 완료
}
