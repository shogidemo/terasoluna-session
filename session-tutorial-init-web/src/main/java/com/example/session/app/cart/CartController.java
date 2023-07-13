package com.example.session.app.cart;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.session.domain.model.Cart;

@Controller
@RequestMapping("cart")
public class CartController {

    // セッションスコープのBeanをDIコンテナから取得する。
    final Cart cart;

    @Inject
    public CartController(Cart cart) {
        this.cart = cart;
    }

    @ModelAttribute
    CartForm setUpForm() {
        return new CartForm();
    }

    @GetMapping
    public String viewCart(Model model) {
        return "cart/viewCart";
    }

    @PostMapping
    public String removeFromCart(@Validated CartForm cartForm,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            ResultMessages messages = ResultMessages.error()
                    .add("e.st.ca.5001");
            model.addAttribute(messages);
            return viewCart(model);
        }
        cart.remove(cartForm.getRemovedItemsIds()); // セッションスコープのBeanのデータを削除する。
        return "redirect:/cart";
    }
}