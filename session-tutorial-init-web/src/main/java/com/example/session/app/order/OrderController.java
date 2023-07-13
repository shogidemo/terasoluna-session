package com.example.session.app.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.session.app.goods.GoodsSearchCriteria;
import com.example.session.domain.model.Cart;
import com.example.session.domain.model.Order;
import com.example.session.domain.service.order.EmptyCartOrderException;
import com.example.session.domain.service.order.InvalidCartOrderException;
import com.example.session.domain.service.order.OrderService;
import com.example.session.domain.service.userdetails.AccountDetails;

@Controller
@RequestMapping("order")
public class OrderController {

    final OrderService orderService;

    // セッションスコープのBeanをDIコンテナから取得する。
    final Cart cart;

    final GoodsSearchCriteria criteria;

    @Autowired
    public OrderController(OrderService orderService, Cart cart, GoodsSearchCriteria criteria) {
        this.orderService = orderService;
        this.cart = cart;
        this.criteria = criteria;
    }

    @GetMapping(params = "confirm")
    public String confirm(@AuthenticationPrincipal AccountDetails userDetails,
                          Model model) {
        if (cart.isEmpty()) {
            ResultMessages messages = ResultMessages.error()
                    .add("e.st.od.5001");
            model.addAttribute(messages);
            return "cart/viewCart";
        }
        model.addAttribute("account", userDetails.getAccount());
        model.addAttribute("signature", cart.calcSignature());
        return "order/confirm";
    }

    @PostMapping
    public String order(@AuthenticationPrincipal AccountDetails userDetails,
                        @RequestParam String signature, RedirectAttributes attributes) {
        Order order = orderService.purchase(
                          userDetails.getAccount(),
                          cart,
                          signature); // ドメイン層にあるServiceのメソッドにて、セッションスコープのBeanの中身を空にしている。
                                      // これによりセッションスコープのBeanの破棄が行われたことになる。
                                      // また、今回のアプリケーションでは、セッションスコープのBeanにある情報をBean破棄後に遷移する画面で使用する。
                                      // そのため、セッションスコープのBeanにあった情報を別のオブジェクトに入れなおしてフラッシュスコープに追加している。
        attributes.addFlashAttribute(order);
        criteria.clear(); // 商品検索情報をデフォルト状態に戻している。
        return "redirect:/order?finish";
    }

    @GetMapping(params = "finish")
    public String finish() {
        return "order/finish";
    }

    // ServiceのメソッドでBusiness例外が発生する可能性があるため、このメソッドでエラーハンドリングを行っている。
    // これにより、Business例外が発生した場合、指定したエラー画面に遷移することになる
    @ExceptionHandler({ EmptyCartOrderException.class,
            InvalidCartOrderException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    ModelAndView handleOrderException(BusinessException e) {
        return new ModelAndView("common/error/businessError").addObject(e.getResultMessages());
    }
}