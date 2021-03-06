package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.net.ServerUtils
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.IReservationView
import com.yywspace.module_home.R
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.view.TimerView

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
                    MaterialDialog(context).show {
                        title(text = "预约")
                        message(text = "是否预约？")
                        positiveButton(R.string.base_dialog_confirm) {

                        }
                        negativeButton(R.string.base_dialog_cancel)
                    }
                }
        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }
}