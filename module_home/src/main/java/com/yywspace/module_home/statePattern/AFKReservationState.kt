package com.yywspace.module_home.statePattern

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.amap.api.services.core.LatLonPoint
import com.yywspace.module_base.MapShowDialogFragment
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.Reservation
import com.yywspace.module_base.bean.Setting
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_base.util.MapUtil
import com.yywspace.module_home.R
import com.yywspace.module_home.databinding.HomeFragmentMainBinding
import com.yywspace.module_home.IReservationView
import com.yywspace.module_home.fragment.HomeFragment
import com.yywspace.module_home.iview.IAFKReservationStateView
import com.yywspace.module_home.presenter.ReservationAFKStatePresenter
import com.yywspace.module_home.presenter.ReservationSignInStatePresenter
import com.yywspace.module_home.view.TimerView

/**
 * 暂离后状态
 */
class AFKReservationState : IReservationState(), IAFKReservationStateView {
    lateinit var context: Context
    lateinit var binding: HomeFragmentMainBinding
    lateinit var reservationView: IReservationView
    private val presenter: ReservationAFKStatePresenter = ReservationAFKStatePresenter()

    init {
        presenter.attachView(this)
    }


    override fun handleView(binding: HomeFragmentMainBinding, inflater: LayoutInflater, reservationView: IReservationView, context: Context) {
        this.context = context
        this.binding = binding
        this.reservationView = reservationView
        val reservation = Reservation.runningReservation!!
        val organization = Reservation.runningOrganization!!

        val titleAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_title_anim)
        binding.homeReservationStatus.apply {
            text = "暂时离开中"
            startAnimation(titleAnimation)
        }
        val timerViewAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_timer_anim)
        binding.homeTimerView.startAnimation(timerViewAnimation)
        binding.homeLocationText.text = Reservation.runningOrganization?.location
        binding.homeLocationText.setOnClickListener {
            if (organization.location.isNotEmpty()) {
                val locationArray = organization.location.split(":")
                if (locationArray.size > 1) {
                    val latLonArray = locationArray[1].split(",")
                    val latLon = LatLonPoint(latLonArray[0].toDouble(), latLonArray[1].toDouble())
                    MapShowDialogFragment
                            .newInstance(latLon.latitude, latLon.longitude, organization.name)
                            .show((context as AppCompatActivity).supportFragmentManager, "map")
                }
            }
        }
        binding.homeSeatText.text = Reservation.runningReservation?.seatName

        val countDownTime = Setting.setting!!.afkTime * 60 // s
        if ((System.currentTimeMillis() - reservation.statusTime) / 1000 > countDownTime) { // 10分钟
            presenter.reservationFailure(context as LifecycleOwner, reservation)
            return
        }
        binding.homeTimerView.apply {
            reset()
            initialTotalSecond = countDownTime - ((System.currentTimeMillis() - reservation.statusTime) / 1000).toInt()
            timeInitial = initialTotalSecond
            mode = TimerView.Mode.TIMER
//            setOnTimeCountDownListener {
//                if (it % 5 == 0.toLong()) {
//                    presenter.updateStatusTime(context as LifecycleOwner, reservation)
//                }
//            }
            setOnTimerStopListener {
                Toast.makeText(context, "暂离失败", Toast.LENGTH_SHORT).show();

            }
            start()
        }

        binding.homeButtonContainer.removeAllViews()
        val view = inflater.inflate(R.layout.home_button_afk, binding.root, false)
        view.findViewById<Button>(R.id.home_release_btn)
                .setOnClickListener {
                    LogUtils.d("释放座位")
                    MaterialDialog(context).show {
                        title(text = "释放座位")
                        message(text = "是否释放座位？")
                        positiveButton(R.string.base_dialog_confirm) {
                            presenter.releaseSeat(context as LifecycleOwner, reservation)
                        }
                        negativeButton(R.string.base_dialog_cancel)
                    }
                }
        view.findViewById<Button>(R.id.home_back_seat_btn)
                .setOnClickListener {
                    LogUtils.d("回归座位")
                    MaterialDialog(context).show {
                        title(text = "回归座位")
                        message(text = "是否回归座位？")
                        positiveButton(R.string.base_dialog_confirm) {
                            if (MapUtil.distance(HomeFragment.latLon!!.latitude, HomeFragment.latLon!!.longitude, HomeFragment.orgLatLon!!.latitude, HomeFragment.orgLatLon!!.longitude) > 100) {
                                Toast.makeText(context, "超出范围无法回归座位", Toast.LENGTH_SHORT).show();
                                return@positiveButton
                            }
                            presenter.backToSeat(context as LifecycleOwner, reservation)
                        }
                        negativeButton(R.string.base_dialog_cancel)
                    }
                }
        binding.homeButtonContainer.addView(view)
        val buttonContainerAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_container_anim)
        binding.homeButtonContainer.startAnimation(buttonContainerAnimation)
    }

    override fun backToSeatResult(response: BaseResponse<Any>, reservation: Reservation) {
        Toast.makeText(context, "暂离", Toast.LENGTH_SHORT).show()
        reservationContext?.currentState = ReservationContext.signInReservationState
        binding.homeTimerView.reset()
        reservationView.refreshLayout()
    }

    override fun releaseSeatResult(response: BaseResponse<Any>, reservation: Reservation) {
        reservationContext?.currentState = ReservationContext.initialReservationState
        binding.homeTimerView.reset()
        reservationView.refreshLayout()
    }

    override fun reservationFailureResult(response: BaseResponse<Any>, reservation: Reservation) {

    }
}