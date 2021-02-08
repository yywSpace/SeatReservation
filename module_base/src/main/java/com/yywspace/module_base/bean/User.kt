package com.yywspace.module_base.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var id: Int? = 0,
                var username: String? = null,
                var message: String? = null,
                var sex: Int? = null,
                var password: String? = null,
                var isAdmin: Boolean = false,
                var avatarPath: String? = null) : Parcelable {
    companion object {
        var currentUser: User? = null
    }

    constructor(username: String?, message: String?, sex: Int?) : this() {
        this.username = username
        this.message = message
        this.sex = sex
    }

    constructor(username: String?, password: String?) : this() {
        this.username = username
        this.password = password
    }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}'
    }
}