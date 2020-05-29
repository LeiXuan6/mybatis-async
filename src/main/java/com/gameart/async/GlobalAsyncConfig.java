package com.gameart.async;

import com.gameart.async.constant.TaskExecutorType;

/**
 * 异步全局配置
 * @author Jack
 * @version 2020/5/29
 */
public class GlobalAsyncConfig {
    /** 任务执行器，默认按表划分 */
    public static final TaskExecutorType TASK_EXECUTOR_TYPE = TaskExecutorType.Class;
}
