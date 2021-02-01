package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.R
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.IReservationView
import com.yywspace.module_home.view.TimerView

/**
 * 暂离后状态
 */
class AFKReservationState : IReservationState() {
    override fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView, context: Context) {
        val titleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_title_anim)
        binding.homeReservationStatus.apply {
            text = "暂时离开中"
            startAnimation(titleAnimation)
        }
        val timerViewAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_timer_anim)
        binding.homeTimerView.startAnimation(timerViewAnimation)
        // TODO: 20-11-22 暂离时间网络读取
        binding.homeTimerView.apply {
            reset()
            initialTotalSecond = 10
            timeInitial = initialTotalSecond
            mode = TimerView.Mode.TIMER
            setOnTimerStopListener {
                Toast.makeText(context, "暂离失败", Toast.LENGTH_SHORT).show();
                // TODO: 20-11-22 暂离失败后界面
            }
            start()
        }

        binding.homeButtonContainer.removeAllViews()
        val view = inflater.inflate(R.layout.home_button_afk, binding.root, false)
        view.findViewById<Button>(R.id.home_release_btn)
                .setOnClickListener {
                    LogUtils.d("释放座位")
                    reservationContext?.currentState = ReservationContext.initialReservationState
                    binding.homeTimerView.reset()
                    reservationView.refreshLayout()
                }
        view.findViewById<Button>(R.id.home_back_seat_btn)
                .setOnClickListener {
                    LogUtils.d("回归座位")
                    reservationContext?.currentState = ReservationContext.signInReservationState
                    binding.homeTimerView.reset()
                    reservationView.refreshLayout()
                }
        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }
}