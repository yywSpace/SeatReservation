package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.R
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.IReservationView
import com.yywspace.module_home.view.TimerView

/**
 * 签到后状态
 */
class SignInReservationState : IReservationState() {
    override fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater,reservationView: IReservationView, context: Context) {
        LogUtils.d("SignInReservationState")
        val titleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_title_anim)
        binding.homeReservationStatus.apply {
            text = "预约正在进行"
            startAnimation(titleAnimation)
        }
        val timerViewAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_timer_anim)
        binding.homeTimerView.startAnimation(timerViewAnimation)
        // todo 网络读取
        binding.homeTimerView.apply {
            initialTotalSecond = 10
//            timeInitial = initialTotalSecond - ((System.currentTimeMillis() - reservation.startTime) / 1000).toInt()
            mode = TimerView.Mode.STOPWATCH
            start()
        }

        binding.homeButtonContainer.removeAllViews()
        val view = inflater.inflate(R.layout.home_button_sign_in, binding.root, false)
        view.findViewById<Button>(R.id.home_sign_out_btn)
                .setOnClickListener {
                    LogUtils.d("签离")
                    reservationContext?.currentState = ReservationContext.initialReservationState
                    binding.homeTimerView.reset()
                    reservationView.refreshLayout()
                }
        view.findViewById<Button>(R.id.home_afk_btn)
                .setOnClickListener {
                    LogUtils.d("暂离")
                    reservationContext?.currentState = ReservationContext.afkReservationState
                    reservationView.refreshLayout()
                }
        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }
}