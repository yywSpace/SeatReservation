package com.yywspace.module_base.bean.scene

data class Seat(var name: String,
                var desc: String,
                var seatMsg: String = "",
                var isBusy: Boolean = false)
