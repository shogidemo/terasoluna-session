package com.example.session.app.goods;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

	final GoodsService goodsService;

	// セッションスコープのBeanをDIコンテナから取得する。
	final Cart cart;

	// セッションスコープのBeanをDIコンテナから取得する。
	final GoodsSearchCriteria criteria;

	@Inject
	public GoodsController(GoodsService goodsService,
						   Cart cart,
						   GoodsSearchCriteria criteria) {
		this.goodsService = goodsService;
		this.cart = cart;
		this.criteria = criteria;
	}

	@ModelAttribute(value = "goodViewForm")
	public GoodViewForm setUpCategoryId() {
		return new GoodViewForm();
	}

	// 通常の商品一覧画面表示処理の前処理を行う。
	// セッションに格納されている商品カテゴリをフォームに、ページ番号をpageableに設定する。
	// 商品カテゴリをフォームに設定するのは、セレクトボックスで表示される商品カテゴリを指定するためである。
	@GetMapping
	public String showGoods(GoodViewForm form, Model model) {
		Pageable pageable = PageRequest.of(criteria.getPage(), 3);
		form.setCategoryId(criteria.getCategoryId());
		return showGoods(pageable, model);
	}

	// カテゴリが変更された時の商品一覧画面表示処理の前処理を行う。
	// 入力された商品カテゴリをセッションに格納する。
	// ページ番号はデフォルトの1ページ目をpageableに指定する。
	@GetMapping(params = "categoryId")
	public String changeCategoryId(GoodViewForm form, Pageable pageable, Model model) {
		criteria.setPage(pageable.getPageNumber());
		criteria.setCategoryId(form.getCategoryId());
		return showGoods(pageable, model);
	}

	// ページが変更された時の商品一覧画面表示処理の前処理を行う。
	// 入力されたページ番号をセッションに格納する。
	// セッションに格納されている商品カテゴリをフォームに設定する。
	@GetMapping(params = "page")
	public String changePage(GoodViewForm form, Pageable pageable, Model model) {
		criteria.setPage(pageable.getPageNumber());
		form.setCategoryId(criteria.getCategoryId());
		return showGoods(pageable, model);
	}

	// 共通部分を扱う。
	// セッションで管理されている商品カテゴリ、前処理で取得したpageableをもとに商品を検索する。
	String showGoods(Pageable pageable, Model model) {
		Page<Goods> page = goodsService.findByCategoryId(
											criteria.getCategoryId()
										   ,pageable);
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