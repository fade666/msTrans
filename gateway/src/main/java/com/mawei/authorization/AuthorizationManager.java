package com.mawei.authorization;

import cn.hutool.core.convert.Convert;

import com.mawei.constant.AuthConstant;
import com.mawei.constant.RedisConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * Created by macro on 2020/6/19.
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        System.out.println(RedisConstant.RESOURCE_ROLES_MAP);
        System.out.println(uri.getPath());

        Map map1 = redisTemplate.opsForHash().entries(RedisConstant.RESOURCE_ROLES_MAP);
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());

        List<String> authorities = Convert.toList(String.class,obj);

        //搞骚操作了 不向管这个序列化的问题，这里直接将list中的【和】符号去掉
        authorities = authorities.stream().map(e->e= e.replace("[","")).collect(Collectors.toList());
        authorities = authorities.stream().map(e->e= e.replace("]","")).collect(Collectors.toList());
        authorities = authorities.stream().map(e->e= e.replace(" ","")).collect(Collectors.toList());

        System.out.println("从redis中读取数据");
        System.out.println(authorities.toString());

        List<String> authList = new ArrayList<>();

        for (String str:authorities) {
            if(str.contains(",")){
                List<String> strings = Arrays.asList(str.split(","));
                authList.addAll(strings);
            }else{
                authList.add(str);
            }
        }
        authList = authList.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());

        System.out.println(authList);


        //认证通过且角色匹配的用户可访问当前路径
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authList::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
