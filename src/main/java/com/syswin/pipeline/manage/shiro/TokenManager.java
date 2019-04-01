package com.syswin.pipeline.manage.shiro;

/**
 * Created by 115477 on 2019/4/1.
 */
public interface TokenManager {
    /**
     * 创建一个token关联上指定用户
     * @param userCode 指定用户的id
     * @return 生成的token
     */
    public StatelessToken createToken(String userCode);

    /**
     * 检查token是否有效
     * @param statelessToken
     * @return 是否有效
     */
    public boolean checkToken(StatelessToken statelessToken);


    /**
     * 检查身份是否有效
     * @param authentication token
     * @return 是否有效
     */
    public boolean check(String authentication);

    /**
     * 从字符串中解析token
     * @param authentication 加密后的字符串
     * @return
     */
    public StatelessToken getToken(String authentication);

    /**
     * 清除token
     * @param userCode 登录用户的id
     */
    public void deleteToken(String userCode);

}
