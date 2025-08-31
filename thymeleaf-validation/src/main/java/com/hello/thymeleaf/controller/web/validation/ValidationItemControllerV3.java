package com.hello.thymeleaf.controller.web.validation;

import com.hello.thymeleaf.domain.Item;
import com.hello.thymeleaf.domain.SaveCheck;
import com.hello.thymeleaf.domain.UpdateCheck;
import com.hello.thymeleaf.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "validation/v3/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable("itemId") long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v3/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "validation/v3/addForm";
  }

//  @PostMapping("/add")
  public String addItem(
      @Validated @ModelAttribute Item item,
      // @ModelAttribute Item item으로 받은 객체는 자동으로 "item"이라는 이름으로 Model에 추가
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ) {

    //특정 필드 예외가 아닌 전체 예외
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10000) {
        bindingResult.reject(
            "totalPriceMin",
            new Object[]{10000, resultPrice},
            null
        );
      }
    }

    if (bindingResult.hasErrors()) {
      log.info("errors: {}", bindingResult);
      return "validation/v3/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v3/items/{itemId}";
  }

  @PostMapping("/add")
  public String addItem2(
      @Validated(SaveCheck.class) @ModelAttribute Item item,
      // @ModelAttribute Item item으로 받은 객체는 자동으로 "item"이라는 이름으로 Model에 추가
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ) {

    //특정 필드 예외가 아닌 전체 예외
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10000) {
        bindingResult.reject(
            "totalPriceMin",
            new Object[]{10000, resultPrice},
            null
        );
      }
    }

    if (bindingResult.hasErrors()) {
      log.info("errors: {}", bindingResult);
      return "validation/v3/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v3/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable("itemId") Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v3/editForm";
  }

//  @PostMapping("/{itemId}/edit")
  public String editItem(
      @PathVariable("itemId") Long itemId,
      @Validated @ModelAttribute Item item,
      BindingResult result
  ) {

    //특정 필드 예외가 아닌 전체 예외
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10000) {
        result.reject(
            "totalPriceMin",
            new Object[]{10000, resultPrice},
            null
        );
      }
    }

    if (result.hasErrors()) {
      log.info("errors: {}", result);
      return "validation/v3/editForm";
    }


    itemRepository.update(itemId, item);
    return "redirect:/validation/v3/items/{itemId}";
  }

  @PostMapping("/{itemId}/edit")
  public String editItem2(
      @PathVariable("itemId") Long itemId,
      @Validated(UpdateCheck.class) @ModelAttribute Item item,
      BindingResult result
  ) {

    //특정 필드 예외가 아닌 전체 예외
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if (resultPrice < 10000) {
        result.reject(
            "totalPriceMin",
            new Object[]{10000, resultPrice},
            null
        );
      }
    }

    if (result.hasErrors()) {
      log.info("errors: {}", result);
      return "validation/v3/editForm";
    }


    itemRepository.update(itemId, item);
    return "redirect:/validation/v3/items/{itemId}";
  }

}
