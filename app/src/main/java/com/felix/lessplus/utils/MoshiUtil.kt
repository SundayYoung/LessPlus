package com.felix.lessplus.utils

import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class MoshiUtil {

    companion object {

        @Throws(RuntimeException::class)
        fun <T> decode(obj: Any): T? {
            val moshi = Moshi.Builder().build()
            val listType = Types.newParameterizedType(List::class.java, obj::class.java)
            val jsonAdapter = moshi.adapter<Type>(listType)
            return jsonAdapter.fromJsonValue(obj) as T
        }
    }
}