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
package com.gameart.async.core;

import com.gameart.async.annotations.AsyncType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 数据库操作的任务
 * @author JackLei
 * @version 2020-04-30
 */
public class DBTask {
    private Class mapperClazz;
    private Method method;
    private Object object;
    private AsyncType type;
    private Object[] args;

    public static DBTask valueOf(Class mapperClazz,Method method,Object obj,AsyncType type,Object[] args){
        DBTask dbTask = new DBTask();
        dbTask.mapperClazz = mapperClazz;
        dbTask.method = method;
        dbTask.object = obj;
        dbTask.type = type;
        dbTask.args = args;
        return dbTask;
    }

    Object invoke() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(object,args);
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getObject() {
        return object;
    }

    public Class getMapperClazz() {
        return mapperClazz;
    }

    public Method getMethod() {
        return method;
    }

    public AsyncType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[" +
                "mapperClazz=" + mapperClazz +
                ", method=" + method +
                ", type=" + type +
                ", args=" + Arrays.toString(args) +
                ']';
    }
}
