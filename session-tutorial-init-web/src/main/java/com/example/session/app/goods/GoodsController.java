package com.example.session.app.goods;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.terasoluna.gfw.common.message.ResultMessages;

import com.example.session.domain.model.Cart;
import com.example.session.domain.model.CartItem;
import com.example.session.domain.model.Goods;
import com.example.session.domain.service.goods.GoodsService;

@Controller
@RequestMapping("goods")
public class GoodsController {

	@Inject
	GoodsService goodsService;

	// セッションスコープのBeanをDIコンテナから取得する。
	@Inject
	Cart cart;

	@ModelAttribute(value = "goodViewForm")
	public GoodViewForm setUpCategoryId() {
		return new GoodViewForm();
	}

	@GetMapping
	String showGoods(GoodViewForm form, Pageable pageable, Model model) {

		Page<Goods> page = goodsService.findByCategoryId(form.getCategoryId(),
				pageable);
		model.addAttribute("page", page);
		return "goods/showGoods";
	}

	@GetMapping("/{goodsId}")
	public String showGoodsDetail(@PathVariable String goodsId, Model model) {

		Goods goods = goodsService.findOne(goodsId);
		model.addAttribute(goods);

		return "goods/showGoodsDetail";
	}

	@PostMapping("/addToCart")
	public String addToCart(@Validated GoodAddForm form, BindingResult result,
							RedirectAttributes attributes) {

		if (result.hasErrors()) {
			ResultMessages messages = ResultMessages.error()
					.add("e.st.go.5001");
			attributes.addFlashAttribute(messages);
			return "redirect:/goods";
		}

		Goods goods = goodsService.findOne(form.getGoodsId());
		CartItem cartItem = new CartItem();
		cartItem.setGoods(goods);
		cartItem.setQuantity(form.getQuantity());
		cart.add(cartItem); // セッションスコープのBeanにデータを追加する。
                            // 画面に情報を表示させるために、オブジェクトをModelに追加する必要はない。

		return "redirect:/goods";
	}
}