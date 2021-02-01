package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.IReservationView
import com.yywspace.module_home.R
import com.yywspace.module_home.databinding.HomeFragmentMainBinding

class InitialReservationState : IReservationState() {
    override fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView, context: Context) {
        val titleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_title_anim)
        binding.homeReservationStatus.apply {
            text = "当前没有任何预约"
            startAnimation(titleAnimation)
        }
        val timerViewAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_timer_anim)
        binding.homeTimerView.startAnimation(timerViewAnimation)
        binding.homeButtonContainer.removeAllViews()

        val view = inflater.inflate(R.layout.home_button_initial, binding.root, false)
        view.findViewById<Button>(R.id.home_reservation_btn)
                .setOnClickListener {
                    LogUtils.d("预约")
                    reservationContext?.currentState = ReservationContext.reservationState
                    reservationView.refreshLayout()
                }
        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }
}