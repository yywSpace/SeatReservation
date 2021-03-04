package com.yywspace.module_base.bean

data class Reservation(
        var id: Int,
        var userId: Int,
        var seatId: Int,
        var seatName: String,
        var startTime: Long,
        var endTime: Long,
        var location: String,
        var status: Int,
        var statusTime: Long) { // 用于各种状态的记时
    // 0 running 1 success 2 fal 3 签到 4 暂离
    companion object {
        var runningReservation: Reservation? = null
    }
}