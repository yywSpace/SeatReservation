package com.yywspace.module_base.bean.scene

data class Seat(var seatName: String,
                var seatDesc: String,
                var seatMsg: String = "",
                var seatStatus: Int = 0)
