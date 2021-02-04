package com.yywspace.module_base.bean

data class Reservation(var startTime: Long, var endTime: Long, var location: String, var seat: String, var status: Int)