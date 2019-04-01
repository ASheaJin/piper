package com.syswin.pipeline.manage.shiro;

import com.syswin.pipeline.manage.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 115477 on 2019/4/1.
 */
@Configuration
public class ShiroConfig {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);
    @Bean
    public SecurityManager securityManager(TokenManager tokenManager, UserService userService){
        // 配置SecurityManager，并注入shiroRealm
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();

        //禁用sessionStorage
        DefaultSubjectDAO de = (DefaultSubjectDAO)securityManager.getSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator =(DefaultSessionStorageEvaluator)de.getSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);

        //realm
        securityManager.setRealm(authorizingRealm(tokenManager, userService));

        //禁止创建session
        StatelessDefaultSubjectFactory subjectFactory = new StatelessDefaultSubjectFactory();
        securityManager.setSubjectFactory(subjectFactory);

        //使用SecurityUtils的静态方法 获取用户等
        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }

    @Bean
    public AuthorizingRealm authorizingRealm(TokenManager tokenManager, UserService userService){
        StatelessRealm realm = new StatelessRealm();
        realm.setTokenManager(tokenManager);
        realm.setUserService(userService);
        return realm;
    }
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, TokenManager tokenManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap();
        filters.put("statelessAuthc", statelessAuthcFilter(tokenManager));
        bean.setFilters(filters);

        //注意是LinkedHashMap 保证有序
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //anon 是默认的匿名拦截器，用于不需拦截的资源
        filterChainDefinitionMap.put("/manage/login", "anon");
        filterChainDefinitionMap.put("/manage/logout", "anon");
        filterChainDefinitionMap.put("/manage/**", "statelessAuthc");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    public StatelessAuthcFilter statelessAuthcFilter(TokenManager tokenManager){
        StatelessAuthcFilter statelessAuthcFilter = new StatelessAuthcFilter();
        statelessAuthcFilter.setTokenManager(tokenManager);
        return statelessAuthcFilter;
    }

    @Bean
    public TokenManager tokenManager(){
        //默认的token管理实现类 32位uuid
        DefaultTokenManagerImpl tokenManager = new DefaultTokenManagerImpl();

        return tokenManager;
    }
}
