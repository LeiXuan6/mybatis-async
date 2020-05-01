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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <ul>
 * 任务生产/消费
 * <li>
 * 生产
 * <li>由上一层提交任务到{@link #taskQueue}</li>
 * </li>
 * <li>
 * 消费
 * <li>线程池{@link AsyncThreadPool}会在指定周期内，消耗里{@link #taskQueue}的任务</li>
 * </li>
 * </ul>
 *
 * @author JackLei
 * @version 2020-04-30
 */
public class AsyncTaskExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskExecutor.class);
    private AtomicBoolean executed = new AtomicBoolean(false);
    private ConcurrentLinkedQueue<DBTask> taskQueue = new ConcurrentLinkedQueue<>();
    /**
     * 一次消费任务的最大数量
     */
    private int threshold = 500;
    private ScheduledFuture scheduledFuture;

    public void start() {
        if (scheduledFuture != null) {
            return;
        }
        scheduledFuture = AsyncThreadPool.executeAtFixedRate(new TaskCommand(), 10, 40, TimeUnit.MILLISECONDS);
    }

    public void addTask(DBTask task) {
        taskQueue.add(task);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("异步队列添加了一个任务{}",task);
        }
    }

    class TaskCommand implements Runnable {

        @Override
        public void run() {

            if (executed.compareAndSet(false, true)) {

                try {
                    int count = 0;
                    for (; ; ) {
                        if (count >= threshold) {
                            LOGGER.error("任务数量超出预期值，预期值 = {} ,队列剩余任务 = {}",threshold,taskQueue.size());
                            break;
                        }

                        DBTask dbTask = taskQueue.poll();
                        if (dbTask == null) {
                            break;
                        }


                        dbTask.invoke();
                        if(LOGGER.isInfoEnabled()){
                            LOGGER.info("执行数据库异步任务[{}]成功",dbTask);
                        }
                        count++;
                    }
                } catch (Exception ex) {
                    LOGGER.error("执行数据库异步操作失败,{}", ex);
                } finally {
                    executed.set(false);
                }

            }

        }
    }

}
