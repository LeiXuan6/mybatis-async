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

import com.gameart.async.SpringContextHolder;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步线程池：处理数据库异步任务
 * @author JackLei
 * @version 2020-04-30
 */
public class AsyncThreadPool {
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private static boolean started;

    public static synchronized void start(){
        if(started){
            return;
        }
        started = true;
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(SpringContextHolder.getDbPoolSize()-1,new NameThreadFactory("DB-ASYNC"));
    }

    public static void stop(){
        scheduledThreadPoolExecutor.shutdown();
    }

    public static void execute(Runnable runnable){
        scheduledThreadPoolExecutor.execute(runnable);
    }

    public static ScheduledFuture executeAtFixedRate(Runnable runnable,
                                                     long initialDelay,
                                                     long period,
                                                     TimeUnit unit){
       return scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable,initialDelay,period,unit);
    }

    static class NameThreadFactory implements ThreadFactory {

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public NameThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + "#" + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
