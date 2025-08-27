package com.hello.thymeleaf.controller.web.validation;

import com.hello.thymeleaf.domain.Item;
import com.hello.thymeleaf.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "validation/v2/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable("itemId") long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v2/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "validation/v2/addForm";
  }

//  @PostMapping("/add")
  public String addItemV1(
      @ModelAttribute Item item, // @ModelAttribute Item item으로 받은 객체는 자동으로 "item"이라는 이름으로 Model에 추가
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes
  ) {
    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.addError(new FieldError("item", "itemName", "상품 이름 입력은 필수입니다."));
    }
    if (item.getPrice() == null || item.getPrice() < 1_000 || item.getPrice() > 1_000_000) {
      bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000원까지 허용합니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9_999) {
      bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999개까지 허용합니다."));
    }

    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10_000) {
        // object의 필드가 아닐 경우,
        bindingResult.addError(
            new ObjectError("item", "가격*수량의 합은 10,000원 이상이어야 합니다. 현재값: " + resultPrice));
      }
    }

    if (bindingResult.hasErrors()) {
      log.info("errors: {}", bindingResult);
      return "validation/v2/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }

  @PostMapping("/add")
  public String addItemV2(
      @ModelAttribute Item item, // @ModelAttribute Item item으로 받은 객체는 자동으로 "item"이라는 이름으로 Model에 추가
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes
  ) {
    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름 입력은 필수입니다."));
    }
    if (item.getPrice() == null || item.getPrice() < 1_000 || item.getPrice() > 1_000_000) {
      bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000원까지 허용합니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9_999) {
      bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999개까지 허용합니다."));
    }

    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10_000) {
        // object의 필드가 아닐 경우,
        bindingResult.addError(
            new ObjectError("item", null, null, "가격*수량의 합은 10,000원 이상이어야 합니다. 현재값: " + resultPrice));
      }
    }

    if (bindingResult.hasErrors()) {
      log.info("errors: {}", bindingResult);
      return "validation/v2/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable("itemId") Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v2/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String editItem(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/validation/v2/items/{itemId}";
  }
}
