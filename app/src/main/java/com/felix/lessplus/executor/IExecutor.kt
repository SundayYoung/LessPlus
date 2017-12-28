package com.felix.lessplus.executor

import com.felix.lessplus.model.bean.Music

/**
 * Created by liuhaiuang on 2017/12/26.
 */
interface IExecutor<T> {
    fun execute()

    fun onPrepare()

    fun onExecuteSuccess(t: Music?)

    fun onExecuteFail(e: Exception)
}
