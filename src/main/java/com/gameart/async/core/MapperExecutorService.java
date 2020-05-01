/*
 * Copyright (c) 2019 The J-Skynet Project
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

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author JackLei
 * @version 2020-05-01
 */
public class MapperExecutorService {
    private static ConcurrentHashMap<Class,AsyncTaskExecutor> executorMap = new ConcurrentHashMap<>();


    public static AsyncTaskExecutor getExecutorWhenNullCreate(Class clazz){
        AsyncTaskExecutor asyncTaskExecutor = executorMap.get(clazz);
        if(asyncTaskExecutor == null){
            asyncTaskExecutor = new AsyncTaskExecutor();
            AsyncTaskExecutor putIfAbsent = executorMap.putIfAbsent(clazz, asyncTaskExecutor);
            if(putIfAbsent != null){
                asyncTaskExecutor = putIfAbsent;
            }
            asyncTaskExecutor.start();
        }
        return asyncTaskExecutor;
    }
}
