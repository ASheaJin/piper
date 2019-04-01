package com.syswin.pipeline.manage.shiro;

/**
 * Created by 115477 on 2019/4/1.
 */
public interface TokenCacheManager {
    /**
     * 根据用户编码获取令牌
     * @param loginName
     * @return
     */
    public String getUserToken(String loginName);

    /**
     * 更新令牌， 每次获取令牌成功时更新令牌失效时间
     * @param loginName
     * @param token
     */
    public void updateUserToken(String loginName, String token);

    /**
     * 删除令牌
     * @param loginName
     */
    public void deleteUserToken(String loginName);

}
