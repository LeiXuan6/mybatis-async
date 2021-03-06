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

import com.gameart.async.SpringContextHolder;
import com.gameart.async.annotations.AsyncMethod;
import com.gameart.async.core.TaskExecutor;
import com.gameart.async.core.DBTask;
import com.gameart.async.core.TableExecutorService;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.Method;

/**
 * mapper的代理
 * @author JackLei
 * @version 2020-04-30
 */
public class MapperInvoker<T> {
    /** 执行器类型 */
    private  T executorType;

    public MapperInvoker(T executorType) {
        this.executorType = executorType;
    }

    @RuntimeType
    public Object invoke(@Origin Method method, @AllArguments @RuntimeType Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        }

        Class<?> mapperClazz = method.getDeclaringClass();
        Object object = SpringContextHolder.getBean(mapperClazz);
        if(method.isAnnotationPresent(AsyncMethod.class)){
            AsyncMethod asyncMethodAnno = method.getAnnotation(AsyncMethod.class);
            TaskExecutor executor= TableExecutorService.getExecutorWhenNullCreate(executorType);
            executor.addTask(DBTask.valueOf(mapperClazz,method,object,asyncMethodAnno.type(),args));
            return null;
        }else {
            return method.invoke(object, args);
        }
    }
}
