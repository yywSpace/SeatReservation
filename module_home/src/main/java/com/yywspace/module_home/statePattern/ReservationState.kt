package com.yywspace.module_home.statePattern

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.R
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.IReservationView
import com.yywspace.module_home.view.TimerView

/**
 * 预约后状态
 */
class ReservationState : IReservationState() {
    override fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView, context: Context) {
        // TODO: 20-11-20 通过网络读取
        val reservation = Reservation(-1,-1,-1,"seatName",1000, 1000, "河南大学", 1);
        val titleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_title_anim)

        binding.homeReservationStatus.apply {
            text = "暂时离开中"
            startAnimation(titleAnimation)
        }
        val timerViewAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_timer_anim)
        binding.homeTimerView.startAnimation(timerViewAnimation)
        binding.homeReservationStatus.text = "预约即将开始"
        binding.homeLocationText.apply {
            text = reservation.location
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8F, resources.displayMetrics)
        }
        binding.homeSeatText.apply {
            text = reservation.seatName
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8F, resources.displayMetrics)
        }
        // todo 网络读取
        binding.homeTimerView.apply {
            initialTotalSecond = 10
            timeInitial = initialTotalSecond
//            timeInitial = initialTotalSecond - ((System.currentTimeMillis() - reservation.startTime) / 1000).toInt()
            mode = TimerView.Mode.TIMER
            setOnTimerStopListener {
                // TODO: 20-11-22 失败后处理
                Toast.makeText(context, "预约失败", Toast.LENGTH_SHORT).show()
                reservationContext?.currentState = ReservationContext.initialReservationState;
                binding.homeTimerView.reset()
                reservationView.refreshLayout()
            }
            start()
        }
        binding.homeButtonContainer.removeAllViews()
        val view = inflater.inflate(R.layout.home_button_reservation, binding.root, false)
        view.findViewById<Button>(R.id.home_un_reservation_btn)
                .setOnClickListener {
                    LogUtils.d("取消预约")
                    reservationContext?.currentState = ReservationContext.initialReservationState;
                    binding.homeTimerView.reset()
                    reservationView.refreshLayout()
                }
        view.findViewById<Button>(R.id.home_sign_in_btn)
                .setOnClickListener {
                    LogUtils.d("签到")
                    this@ReservationState.reservationContext?.currentState = ReservationContext.signInReservationState
                    binding.homeTimerView.reset()
                    reservationView.refreshLayout()
                }

        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }
}