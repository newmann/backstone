/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.service;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.security.Cryptos;
import com.beiyelin.shop.common.utils.IdGen;
import com.beiyelin.shop.common.utils.JedisUtils;
import com.beiyelin.shop.common.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sun.util.locale.provider.JRELocaleConstants;

import java.util.concurrent.TimeUnit;


/**
 * 用户登录的token处理
 * 用户包括个人用户和管理员用户
 * 同一时间可能有多个客户端同时登录，每个客户端会分配一个token，方便处理客户端的并行工作
 *
 * @author Newmann
 * @version 2017-03-18
 */
@Service
public class AppLoginService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 生成APP用户登录令牌
     */
    public String genAppLoginToken() {
        return IdGen.uuid();
    }

    /**
     * 将id对应的token保存到redis中去
     * @param userId
     * @param token
     * @return
     */

    public void updateAppLoginToken(String userId,String token){

        redisTemplate.opsForValue().set(token,userId, StrUtils.toInteger(Global.getConfig("token.TimeoutClean")), TimeUnit.SECONDS);
//        JedisUtils.set(token,userId, StrUtils.toInteger(Global.getConfig("token.TimeoutClean")));
    }

    /**
     * 根据token找到对应的userId，如果不存在，则表示没有登录；如果不相等，也表示没有登录
     * @param userId
     * @param token
     * @return
     */
    public boolean isAppLoggedIn(String userId,String token){
        String id;
        id = redisTemplate.opsForValue().get(token);

//        id= JedisUtils.get(token);
        if (id == null){
            return false;
        }else{
            return id.equals(userId);
        }

    }

    /**
     * 刪除token，就表示已經退出系統了。
     *
     * @param userId
     * @param token
     */
    public boolean logout(String userId,String token){
        String id;
        id = redisTemplate.opsForValue().get(token).toString();
        if (id.equals(null)){
            return false;
        }else{
            try {
                redisTemplate.delete(token);
                return true;
            } catch (Exception ex){
                return false;
            }
        }

//        if (JedisUtils.get(token).equals(userId)) {
//            JedisUtils.del(token);
//        }
    }

}