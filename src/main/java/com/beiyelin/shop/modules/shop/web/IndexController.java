/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.web;

import com.beiyelin.shop.modules.shop.entity.ShopCategory;
import com.beiyelin.shop.modules.shop.entity.ShopProduct;
import com.beiyelin.shop.modules.shop.service.ShopProductService;
import com.google.common.collect.Lists;
import com.beiyelin.shop.common.web.BaseController;
import com.beiyelin.shop.modules.shop.utils.ShopCategoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 网站首页Controller
 * @author Tony Wong
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController {

	private static final String VIEW_PATH = "modules/shop/index/";

	@Autowired
	private ShopProductService productService;

	/**
	 * 网站首页
	 */
	@RequestMapping
	public String index(Model model) {
		addGlobalPath(model);
		List<ShopCategory> firstCategoryList = ShopCategoryUtils.findFirstList();
		List<ShopProduct> featuredHomeDayProductList = productService.findFeaturedHomeDay();
        List<ShopProduct> featuredHomeSpecialProductList = productService.findFeaturedHomeSpecial();

        model.addAttribute("firstCategoryList", firstCategoryList);
		model.addAttribute("featuredHomeDayProductList", featuredHomeDayProductList);
        model.addAttribute("featuredHomeSpecialProductList", featuredHomeSpecialProductList);
		return VIEW_PATH + "index.html";
	}
	
}
