/*
 * Copyright (c) 2020 The mybatis-async Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gameart.async.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JackLei
 * @version 2020-04-30
 */
public class ProxyFactory<I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyFactory.class);
    private  final Map<Class,Object> CACHE_MAP = new ConcurrentHashMap<>();
    private final Class<I> interfaceClass;

    public ProxyFactory(Class<I> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public static <I> ProxyFactory<I> factory(Class<I> interfaceClass){
        ProxyFactory factory = new ProxyFactory(interfaceClass);
        return factory;
    }

    public I newInstance(){
        Object proxy = CACHE_MAP.get(interfaceClass);
        if(proxy != null){
            return (I) proxy;
        }

        MapperInvoker handler = new MapperInvoker();
        Class<? extends I> cls = new ByteBuddy()
                .subclass(interfaceClass)
                .method(ElementMatchers.isDeclaredBy(interfaceClass))
                .intercept(MethodDelegation.to(handler, "handler"))
                .make()
                .load(interfaceClass.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();

        try {
            proxy = cls.newInstance();
            Object putIfAbsent = CACHE_MAP.putIfAbsent(interfaceClass, proxy);
            if(putIfAbsent != null){
                proxy = putIfAbsent;
            }
            return (I) proxy;
        } catch (Throwable t) {
            LOGGER.error("代理对象创建失败,class = {}",interfaceClass,t);
        }
        return null;
    }

}
