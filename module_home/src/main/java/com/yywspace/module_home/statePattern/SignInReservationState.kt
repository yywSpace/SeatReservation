package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_home.R
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.IReservationView
import com.yywspace.module_home.iview.ISignInReservationStateView
import com.yywspace.module_home.presenter.ReservationSignInStatePresenter
import com.yywspace.module_home.presenter.ReservationStatePresenter
import com.yywspace.module_home.view.TimerView

/**
 * 签到后状态
 */
class SignInReservationState : IReservationState(), ISignInReservationStateView {
    lateinit var context: Context
    lateinit var binding: HomeFragmentMainBinding
    lateinit var reservationView: IReservationView
    private val presenter: ReservationSignInStatePresenter = ReservationSignInStatePresenter()

    init {
        presenter.attachView(this)
    }


    override fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView, context: Context) {
        this.context = context
        this.binding = binding
        this.reservationView = reservationView
        val reservation = Reservation.runningReservation!!
        LogUtils.d("SignInReservationState")

        val titleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_title_anim)
        binding.homeReservationStatus.apply {
            text = "预约正在进行"
            startAnimation(titleAnimation)
        }
        val timerViewAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_timer_anim)
        binding.homeTimerView.startAnimation(timerViewAnimation)


        binding.homeTimerView.apply {
            if (reservation.endTime > 0)
                initialTotalSecond = ((reservation.endTime - reservation.startTime) / 1000).toInt()
//            timeInitial = initialTotalSecond - ((System.currentTimeMillis() - reservation.startTime) / 1000).toInt()
            mode = TimerView.Mode.STOPWATCH
            setOnTimeCountDownListener {
                if (it % 5 == 0.toLong()) {
                    presenter.updateEndTime(context as LifecycleOwner, reservation)
                }
            }
            start()
        }

        binding.homeButtonContainer.removeAllViews()
        val view = inflater.inflate(R.layout.home_button_sign_in, binding.root, false)
        view.findViewById<Button>(R.id.home_sign_out_btn)
                .setOnClickListener {
                    LogUtils.d("签离")
                    MaterialDialog(context).show {
                        title(text = "签离")
                        message(text = "是否签离？")
                        positiveButton(R.string.base_dialog_confirm) {
                            binding.homeTimerView.setOnTimeCountDownListener {

                            }
                            // 取消预约
                            presenter.reservationSignOut(context as LifecycleOwner, reservation)
                        }
                        negativeButton(R.string.base_dialog_cancel)
                    }
                }
        view.findViewById<Button>(R.id.home_afk_btn)
                .setOnClickListener {
                    LogUtils.d("暂离")
                    MaterialDialog(context).show {
                        title(text = "暂离")
                        message(text = "是否暂离？")
                        positiveButton(R.string.base_dialog_confirm) {
                            binding.homeTimerView.setOnTimeCountDownListener {

                            }
                            // 取消预约
                            presenter.reservationAFK(context as LifecycleOwner, reservation)
                        }
                        negativeButton(R.string.base_dialog_cancel)
                    }
                }
        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }

    override fun updateEndTimeResult(response: BaseResponse<Any>, reservation: Reservation) {
        LogUtils.d("updateEndTime")
    }

    override fun reservationSignOutResult(response: BaseResponse<Any>, reservation: Reservation) {
        Toast.makeText(context, "签离", Toast.LENGTH_SHORT).show()
        reservationContext?.currentState = ReservationContext.initialReservationState
        binding.homeTimerView.reset()
        reservationView.refreshLayout()
    }

    override fun reservationAFKResult(response: BaseResponse<Any>, reservation: Reservation) {
        Toast.makeText(context, "暂离", Toast.LENGTH_SHORT).show()
        reservationContext?.currentState = ReservationContext.afkReservationState
        reservationView.refreshLayout()
    }

    override fun reservationFailureResult(response: BaseResponse<Any>, reservation: Reservation) {

    }
}