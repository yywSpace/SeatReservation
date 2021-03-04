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
import com.yywspace.module_home.iview.IReservationStateView
import com.yywspace.module_home.presenter.ReservationStatePresenter
import com.yywspace.module_home.view.TimerView

/**
 * 预约后状态
 */
class ReservationState : IReservationState(), IReservationStateView {
    lateinit var context: Context
    lateinit var binding: HomeFragmentMainBinding
    lateinit var reservationView: IReservationView
    private val presenter: ReservationStatePresenter = ReservationStatePresenter()

    init {
        presenter.attachView(this)
    }

    override fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView, context: Context) {
        this.context = context
        this.binding = binding
        this.reservationView = reservationView
        val reservation = Reservation.runningReservation!!
        val titleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_title_anim)

        binding.homeReservationStatus.apply {
            text = "预约即将开始"
            startAnimation(titleAnimation)
        }
        val timerViewAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_timer_anim)
        binding.homeTimerView.startAnimation(timerViewAnimation)
        binding.homeLocationText.apply {
            text = reservation.location
        }
        binding.homeSeatText.apply {
            text = reservation.seatName
        }
        // todo 网络读取预约倒计时
        val countDownTime = 60 * 10 // s
        if ((System.currentTimeMillis() - reservation.statusTime) / 1000 > countDownTime) { // 10分钟
            presenter.reservationFailure(context as LifecycleOwner, reservation)
            return
        }
        binding.homeTimerView.apply {
            initialTotalSecond = countDownTime - ((System.currentTimeMillis() - reservation.statusTime) / 1000).toInt()
            timeInitial = initialTotalSecond
//            timeInitial = initialTotalSecond - ((System.currentTimeMillis() - reservation.startTime) / 1000).toInt()
            mode = TimerView.Mode.TIMER
//            setOnTimeCountDownListener {
//                if (it % 5 == 0.toLong()) {
//                    presenter.updateStatusTime(context as LifecycleOwner, reservation)
//                }
//            }
            setOnTimerStopListener {
                // 预约失败
                presenter.reservationFailure(context as LifecycleOwner, reservation)
            }

            start()
        }
        binding.homeButtonContainer.removeAllViews()
        val view = inflater.inflate(R.layout.home_button_reservation, binding.root, false)
        view.findViewById<Button>(R.id.home_un_reservation_btn)
                .setOnClickListener {
                    MaterialDialog(context).show {
                        title(text = "取消预约")
                        message(text = "是否取消预约？")
                        positiveButton(R.string.base_dialog_confirm) {
                            // 取消预约
                            presenter.cancelReservation(context as LifecycleOwner, reservation)
                        }
                        negativeButton(R.string.base_dialog_cancel)
                    }

                }
        view.findViewById<Button>(R.id.home_sign_in_btn)
                .setOnClickListener {
                    MaterialDialog(context).show {
                        title(text = "签到")
                        message(text = "是否签到？")
                        positiveButton(R.string.base_dialog_confirm) {
                            presenter.reservationSignIn(context as LifecycleOwner, reservation)
                        }
                        negativeButton(R.string.base_dialog_cancel)
                    }

                }

        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }

    override fun cancelReservationResult(response: BaseResponse<Any>, reservation: Reservation) {
        Reservation.runningReservation = null
        reservationContext?.currentState = ReservationContext.initialReservationState;
        binding.homeTimerView.reset()
        reservationView.refreshLayout()
    }

    override fun getRunningReservationResult(reservation: Reservation) {

    }

    override fun reservationSignInResult(response: BaseResponse<Any>, reservation: Reservation) {
        Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show()
        reservationContext?.currentState = ReservationContext.signInReservationState
        Reservation.runningReservation = reservation
        binding.homeTimerView.reset()
        reservationView.refreshLayout()
    }

    override fun reservationFailureResult(response: BaseResponse<Any>, reservation: Reservation) {
        Toast.makeText(context, "预约失败", Toast.LENGTH_SHORT).show()
        Reservation.runningReservation = null
        reservationContext?.currentState = ReservationContext.initialReservationState
        binding.homeTimerView.reset()
        reservationView.refreshLayout()
    }

//    override fun updateStatusTimeResult(response: BaseResponse<Any>, reservation: Reservation) {
//        LogUtils.d("updateStatusTimeResult")
//    }
}