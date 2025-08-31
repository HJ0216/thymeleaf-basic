package com.hello.thymeleaf.controller.web.validation;

import com.hello.thymeleaf.controller.web.validation.form.ItemSaveForm;
import com.hello.thymeleaf.controller.web.validation.form.ItemUpdateForm;
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
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "validation/v4/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable("itemId") long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v4/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "validation/v4/addForm";
  }

  @PostMapping("/add")
  public String addItem(
      @Validated @ModelAttribute("item") ItemSaveForm form,
      // @ModelAttribute name을 지정하지 않을 경우, 객체이름 첫글자만 소문자로 변경해서 들어감
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ) {

    //특정 필드 예외가 아닌 전체 예외
    if (form.getPrice() != null && form.getQuantity() != null) {
      int resultPrice = form.getPrice() * form.getQuantity();
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
      return "validation/v4/addForm";
    }

    Item item = new Item(form.getItemName(), form.getPrice(), form.getQuantity());

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v4/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable("itemId") Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v4/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String editItem(
      @PathVariable("itemId") Long itemId,
      @Validated @ModelAttribute("item") ItemUpdateForm form,
      BindingResult result
  ) {

    //특정 필드 예외가 아닌 전체 예외
    if (form.getPrice() != null && form.getQuantity() != null) {
      int resultPrice = form.getPrice() * form.getQuantity();
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
      return "validation/v4/editForm";
    }

    Item itemParam = new Item(form.getItemName(), form.getPrice(), form.getQuantity());

    itemRepository.update(itemId, itemParam);
    return "redirect:/validation/v4/items/{itemId}";
  }

}
