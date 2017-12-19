package com.felix.lessplus.http


import android.util.Log

import com.felix.lessplus.utils.JsonUtil

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.HttpURLConnection

import retrofit2.Call
import retrofit2.Callback

/**
 * Created by liuhaiyang on 2016/9/6.
 */
abstract class RestBaseCallBack<D> : Callback<BaseServerResponse> {

    override fun onResponse(call: Call<BaseServerResponse>, response: retrofit2.Response<BaseServerResponse>) {

        if (response.code() != HttpURLConnection.HTTP_OK) {
            onFailure(null, NET_ERROR_CODE, NET_ERROR_MSG)
            return
        }

        if (response.body() == null) {
            return
        }

        val baseResponse = response.body() ?: return

        var d: D? = null
        try {
            d = JsonUtil.decode(baseResponse.data, type)
        } catch (e: Exception) {
            Log.e("HttpResponse:", e.toString())
        }

        onResponse(d)

    }

    override fun onFailure(call: Call<BaseServerResponse>, t: Throwable) {
        onFailure(t, NET_ERROR_CODE, NET_ERROR_MSG)
    }


    abstract fun onResponse(data: D?)

    abstract fun onFailure(error: Throwable?, code: Int, msg: String)

    /**
     * 获取带泛型参数的父类参数类型
     *
     * @return 父类泛型参数
     */
    private val type: Type
        get() {
            var superclass = javaClass.genericSuperclass
            while (superclass is Class<*> && superclass != RestBaseCallBack::class.java) {
                superclass = superclass.genericSuperclass
            }
            if (superclass is Class<*>) {
                throw RuntimeException("Missing type parameter.")
            }
            val parameterized = superclass as ParameterizedType
            return parameterized.actualTypeArguments[0]
        }

    companion object {

        private val NET_ERROR_CODE = 445566
        private val NET_ERROR_MSG = "网络出错，请检查网络连接"
    }
}
