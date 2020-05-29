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
package com.gameart.async;

import com.gameart.async.core.AsyncThreadPool;
import com.gameart.async.mapper.UserMapper;
import com.gameart.async.proxy.ProxyFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author JackLei
 * @version 2020-04-19
 */
public class ProxyTest {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath*:applicationContext-mybatis.xml");
        AsyncThreadPool.start();
        for(int i = 0 ; i < 1000; i ++) {
            UserMapper userMapper = ProxyFactory.factory(UserMapper.class).newInstance();

            String id = String.valueOf(i);
            userMapper.insert(id, "1");
            userMapper.update(id, "proxy");
        }
    }

}
