/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.app.web;

import com.beiyelin.shop.modules.sys.utils.AreaUtils;
import com.beiyelin.shop.modules.sys.entity.Area;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 测试用的Controller
 * @author Tony Wong
 * @version 2015-04-19
 */
@Controller
@RequestMapping("/app")
public class AppDemo2Controller extends AppBaseController {

	/**
	 * 级联选择
	 */
	@RequestMapping("/news")
	public String view(ModelMap m) {
		List<Area> provinceList = AreaUtils.findByParentId(Area.PROVINCE_PARENT_ID);
		m.put("provinceList", provinceList);
		return "modules/app/demo2/news";
	}

	/**
	 * 级联选择
	 */
	@RequestMapping("/life")
	public String life(ModelMap m) {
		List<Area> provinceList = AreaUtils.findByParentId(Area.PROVINCE_PARENT_ID);
		m.put("provinceList", provinceList);
		return "modules/app/demo2/life";
	}
}
