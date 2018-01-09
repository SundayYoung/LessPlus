package com.felix.lessplus.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.Fragment

import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet
import java.util.Random

/**
 * PermissionsUtil.with(this).addPermission(Manifest.permission.CAMERA).setCallback(new PermissionsUtil.Callback(){}).request();
 *
 * Don't forget PermissionsUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults); in onRequestPermissionsResult
 *
 * Created by liuhaiyang on 2017/8/16.
 */

object PermissionsUtil {
    private val TAG = "PermissionUtil"

    private val sPendingRequest = ArrayList<PermissionItem<*>>()


    fun <T> with(context: T?): PermissionItem<*> {
        if (context == null) {
            throw NullPointerException("context is null")
        }
        if (context !is Activity && context !is Fragment) {
            throw IllegalArgumentException("context must be Activity or Fragment")
        }

        val item = findPermissionItemOf(context)
        return item ?: pushRequest(context)
    }

    private fun <T> pushRequest(context: T): PermissionItem<T> {
        val newItem = PermissionItem<T>()
        newItem.mPermissionContext = WeakReference(context)
        sPendingRequest.add(newItem)
        return newItem
    }

    private fun <T> findPermissionItemOf(context: T): PermissionItem<*>? {
        for (item in sPendingRequest) {
            if (item.mPermissionContext!!.get() != null && item.mPermissionContext!!.get() === context) {
                return item
            }
        }
        return null
    }

    fun onRequestPermissionsResult(context: Activity, requestCode: Int, permissions: Array<String>,
                                   grantResults: IntArray) {
        val item = findPermissionItemOf(context) ?: return
        if (requestCode != item.mRequestCode) {
            return
        }
        sPendingRequest.remove(item)

        val denied = HashSet<String>()
        var i = 0
        val length = permissions.size
        while (i < length) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                denied.add(permissions[i])
            }
            i++
        }
        if (item.mCallback != null) {
            if (denied.size != 0) {
                item.mCallback!!.onPermissionDenied(denied.toTypedArray())
            } else {
                item.mCallback!!.onPermissionGranted(item.mPermissions.toTypedArray())
            }
        }
    }


    class PermissionItem<T> {
        var mPermissionContext: WeakReference<T>? = null
        var mRequestCode: Int = 0
        var mCallback: Callback? = null
        val mPermissions = HashSet<String>()

        init {
            mRequestCode = Random().nextInt(100) + 100
        }

        fun addPermission(permission: String): PermissionItem<T> {
            mPermissions.add(permission)
            return this
        }

        fun addPermissions(permissions: Array<String>): PermissionItem<T> {
            mPermissions.addAll(Arrays.asList(*permissions))
            return this
        }

        fun addPermissions(permissions: Collection<String>): PermissionItem<T> {
            mPermissions.addAll(permissions)
            return this
        }

        fun setCallback(callback: Callback): PermissionItem<T> {
            mCallback = callback
            return this
        }

        fun setRequestCode(requestCode: Int): PermissionItem<T> {
            mRequestCode = requestCode
            return this
        }

        fun request() {
            if (mPermissions.size == 0) {
                if (mCallback != null) {
                    mCallback!!.onPermissionGranted(arrayOfNulls(0))
                }
            } else {
                if (mPermissionContext!!.get() == null) {
                    return
                }

                val denied = HashSet<String>()
                for (permission in mPermissions) {
                    if (checkContextPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        denied.add(permission)
                    }
                }
                if (denied.size == 0) {
                    if (mCallback != null) {
                        mCallback!!.onPermissionGranted(mPermissions.toTypedArray())
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestContextPermissions(denied.toTypedArray(), mRequestCode)
                    } else {
                        if (mCallback != null) {
                            mCallback!!.onPermissionDenied(denied.toTypedArray())
                        }
                    }
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        private fun requestContextPermissions(permissions: Array<String>, requestCode: Int) {
            val context = mPermissionContext!!.get()
            if (context is Activity) {
                (context as Activity).requestPermissions(permissions, requestCode)
            } else if (context is Fragment) {
                (context as Fragment).requestPermissions(permissions, requestCode)
            }
        }

        private fun checkContextPermission(permission: String): Int {
            val context = mPermissionContext!!.get()
            if (context is Activity) {
                return (context as Activity).checkCallingOrSelfPermission(permission)
            } else if (context is Fragment) {
                return (context as Fragment).activity!!.checkCallingOrSelfPermission(permission)
            }
            return -1
        }
    }

    interface Callback {
        fun onPermissionGranted(permissions: Array<String?>)

        fun onPermissionDenied(permissions: Array<String>)
    }
}
