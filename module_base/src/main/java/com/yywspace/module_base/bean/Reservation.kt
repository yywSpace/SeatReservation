package com.yywspace.module_base.bean

data class Reservation(
        var id: Int,
        var userId: Int,
        var seatId: Int,
        var seatName: String,
        var startTime: Long,
        var endTime: Long,
        var location: String,
        var status: Int) {
    // 0 running 1 success 2 fal
    companion object {
        var runningReservation: Reservation? = null
    }
}