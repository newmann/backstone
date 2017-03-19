/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.utils;

import com.beiyelin.shop.common.service.BaseService;
import com.beiyelin.shop.common.utils.CacheUtils;
import com.beiyelin.shop.common.utils.SpringContextHolder;
import com.beiyelin.shop.modules.sys.dao.PersonDao;
import com.beiyelin.shop.modules.sys.entity.Person;
import com.beiyelin.shop.modules.sys.security.SystemAuthorizingRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * 个人工具类
 * @author Newmann
 * @version 2017-03-18
 */
public class PersonUtils {

	private static PersonDao personDao = SpringContextHolder.getBean(PersonDao.class);
//	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
//	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
//	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
//	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	public static final String PERSON_CACHE = "personCache";
	public static final String PERSON_CACHE_ID_ = "id_";
	public static final String PERSON_CACHE_LOGIN_NAME_ = "ln";
	public static final String PERSON_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static Person get(String id){
		Person person = (Person) CacheUtils.get(PERSON_CACHE, PERSON_CACHE_ID_ + id);
		if (person ==  null){
			person = personDao.get(id);
			if (person == null){
				return null;
			}
//			person.setRoleList(roleDao.findList(new Role(person)));
			CacheUtils.put(PERSON_CACHE, PERSON_CACHE_ID_ + person.getId(), person);
			CacheUtils.put(PERSON_CACHE, PERSON_CACHE_LOGIN_NAME_ + person.getLoginName(), person);
		}
		return person;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static Person getByLoginName(String loginName){
		Person person = (Person)CacheUtils.get(PERSON_CACHE, PERSON_CACHE_LOGIN_NAME_ + loginName);
		if (person == null){
			person = personDao.getByLoginName(new Person(null, loginName));
			if (person == null){
				return null;
			}
//			person.setRoleList(roleDao.findList(new Role(person)));
			CacheUtils.put(PERSON_CACHE, PERSON_CACHE_ID_ + person.getId(), person);
			CacheUtils.put(PERSON_CACHE, PERSON_CACHE_LOGIN_NAME_ + person.getLoginName(), person);
		}
		return person;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
//		removeCache(CACHE_ROLE_LIST);
//		removeCache(CACHE_MENU_LIST);
//		removeCache(CACHE_AREA_LIST);
//		removeCache(CACHE_OFFICE_LIST);
//		removeCache(CACHE_OFFICE_ALL_LIST);
		PersonUtils.clearCache(getPerson());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param person
	 */
	public static void clearCache(Person person){
		CacheUtils.remove(PERSON_CACHE, PERSON_CACHE_ID_ + person.getId());
		CacheUtils.remove(PERSON_CACHE, PERSON_CACHE_LOGIN_NAME_ + person.getLoginName());
		CacheUtils.remove(PERSON_CACHE, PERSON_CACHE_LOGIN_NAME_ + person.getOldLoginName());
//		if (person.getOffice() != null && person.getOffice().getId() != null){
//			CacheUtils.remove(PERSON_CACHE, PERSON_CACHE_LIST_BY_OFFICE_ID_ + person.getOffice().getId());
//		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new Person()
	 */
	public static Person getPerson(){
		SystemAuthorizingRealm.Principal principal = getPrincipal();
		if (principal!=null){
			Person person = get(principal.getId());
			if (person != null){
				return person;
			}
			return new Person();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new Person();
	}

	/**
	 * 获取当前用户ID
	 */
	public static String getPersonId(){
		SystemAuthorizingRealm.Principal principal = getPrincipal();
		if (principal!=null){
			return principal.getId();
		} else {
            return null;
        }
	}

	/**
	 * 用户是否已经登录
	 * @return
	 */
	public static boolean isLoggedIn() {
		SystemAuthorizingRealm.Principal principal = getPrincipal();
		if (principal != null)
			return true;
		else
			return false;
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
//	public static List<Role> getRoleList(){
//		@SuppressWarnings("unchecked")
//		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
//		if (roleList == null){
//			Person person = getUser();
//			if (person.isAdmin()){
//				roleList = roleDao.findAllList(new Role());
//			}else{
//				Role role = new Role();
//				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(person.getCurrentUser(), "o", "u"));
//				roleList = roleDao.findList(role);
//			}
//			putCache(CACHE_ROLE_LIST, roleList);
//		}
//		return roleList;
//	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
//	public static List<Menu> getMenuList(){
//		@SuppressWarnings("unchecked")
//		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
//		if (menuList == null){
//			Person person = getUser();
//			if (person.isAdmin()){
//				menuList = menuDao.findAllList(new Menu());
//			}else{
//				Menu m = new Menu();
//				m.setUserId(person.getId());
//				menuList = menuDao.findByUserId(m);
//			}
//			putCache(CACHE_MENU_LIST, menuList);
//		}
//		return menuList;
//	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
//	public static List<Area> getAreaList(){
//		@SuppressWarnings("unchecked")
//		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
//		if (areaList == null){
//			areaList = areaDao.findAllList(new Area());
//			putCache(CACHE_AREA_LIST, areaList);
//		}
//		return areaList;
//	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
//	public static List<Office> getOfficeList(){
//		@SuppressWarnings("unchecked")
//		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
//		if (officeList == null){
//			Person person = getUser();
//			if (person.isAdmin()){
//				officeList = officeDao.findAllList(new Office());
//			}else{
//				Office office = new Office();
//				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(person, "a", ""));
//				officeList = officeDao.findList(office);
//			}
//			putCache(CACHE_OFFICE_LIST, officeList);
//		}
//		return officeList;
//	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
//	public static List<Office> getOfficeAllList(){
//		@SuppressWarnings("unchecked")
//		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
//		if (officeList == null){
//			officeList = officeDao.findAllList(new Office());
//		}
//		return officeList;
//	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static SystemAuthorizingRealm.Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== Person Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
//		getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}
	
//	public static Map<String, Object> getCacheMap(){
//		Principal principal = getPrincipal();
//		if(principal!=null){
//			return principal.getCacheMap();
//		}
//		return new HashMap<String, Object>();
//	}
	
}
