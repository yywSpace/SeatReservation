package com.yywspace.module_mine.user.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.color.ColorPalette
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.util.LogUtils
import com.yywspace.module_base.util.ViewScreenShotUtils
import com.yywspace.module_mine.R
import com.yywspace.module_mine.databinding.MineUserInfoStatisticAnalysisBinding
import com.yywspace.module_mine.iview.IStatisticView
import com.yywspace.module_mine.presenter.StatisticPresenter
import com.yywspace.module_mine.user.adapter.UserInfPieChartItemListAdapter
import com.yywspace.module_base.bean.statistic.StatisticReservation
import com.yywspace.module_base.bean.statistic.StatisticOverview
import com.yywspace.module_base.bean.statistic.StatisticOrganization
import com.yywspace.module_base.util.TimeUtils
import java.nio.file.Files
import java.nio.file.Paths


class UserInfoStatisticAnalysisActivity : BaseActivity<IStatisticView, StatisticPresenter>(), IStatisticView {
    private lateinit var binding: MineUserInfoStatisticAnalysisBinding
    lateinit var pieItemListAdapter: UserInfPieChartItemListAdapter
    var userId = -1

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mine_statistic_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        LogUtils.d("onOptionsItemSelected")
        return when (item.itemId) {
            R.id.action_share -> {
                val screenShot = ViewScreenShotUtils.scrollViewScreenShot(binding.nestedScrollView)
                val imgPath = saveBitmap2Gallery(baseContext, screenShot!!)
                LogUtils.d(imgPath.toString())
                if (imgPath != null) {
                    startImageShareIntent(imgPath.toString())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDiskCacheDir(context: Context): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            context.externalCacheDir!!.path
        } else {
            context.cacheDir.path
        }
    }

    // 保存图片到手机指定目录
    private fun saveBitmapToCache(imgName: String, bytes: ByteArray): String {
        val filePath = getDiskCacheDir(baseContext) + "/shareScreenShot"
        val imgDir = Paths.get(filePath)
        if (!Files.exists(imgDir)) Files.createDirectory(imgDir)
        val imgPath = Paths.get("$filePath/$imgName")
        if (Files.exists(imgPath)) Files.delete(imgPath)
        with(Files.newOutputStream(imgPath)) {
            write(bytes)
        }
        return imgPath.toAbsolutePath().toString()
    }

    private fun saveBitmap2Gallery(context: Context, bitmap: Bitmap): Uri? {
        val name = System.currentTimeMillis().toString()
        val photoPath = Environment.DIRECTORY_DCIM + "/share"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, photoPath)//保存路径
                put(MediaStore.MediaColumns.IS_PENDING, true)
            }
        }
        val insert = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ) ?: return null //为空的话 直接失败返回了
        //这个打开了输出流  直接保存图片就好了
        context.contentResolver.openOutputStream(insert).use {
            it ?: return null
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, false)
        }
        return insert
    }

    private fun startImageShareIntent(imgPath: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgPath))
        startActivity(Intent.createChooser(intent, "share this image to..."));
    }

    override fun getLayout(inflater: LayoutInflater): View {
        binding = MineUserInfoStatisticAnalysisBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createPresenter(): StatisticPresenter {
        return StatisticPresenter()
    }

    override fun createView(): IStatisticView {
        return this
    }

    override fun init() {
        userId = intent.getIntExtra("user_id", 1)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "统计分析"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.lineChart.description = Description().apply {
            text = ""
        }

        binding.pieChart.centerText = "时间比例"
        binding.pieChart.description = Description().apply {
            text = ""
        }
        pieItemListAdapter = UserInfPieChartItemListAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pieItemListAdapter
        }
        presenter.getStatisticLineDataList(this, userId)
        presenter.getStatisticPieDataList(this, userId, 5)
        presenter.getStatisticOverview(this, userId)
    }

    override fun getStatisticLineDataListResult(reservationInfoList: List<StatisticReservation>?) {
        if (reservationInfoList != null) {
            val entries = mutableListOf<Entry>()
            val xLabel = mutableListOf<String>()
            for ((index, data) in reservationInfoList.withIndex()) {
                entries.add(Entry(index.toFloat(), data.time / 1000 / 60f))
                xLabel.add(TimeUtils.longToString(data.day, "MM-dd"))
            }
            val dataSet = LineDataSet(entries, "时间")
            val lineData = LineData(dataSet)
            binding.lineChart.data = lineData
            binding.lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return xLabel[value.toInt()]
                    }
                }
            }
            binding.lineChart.invalidate()
        }
    }

    override fun getStatisticPieDataListResult(pieDataList: List<StatisticOrganization>?) {
        val pieEntries: MutableList<PieEntry> = ArrayList()
        if (pieDataList != null) {
            val totalTime = pieDataList.stream().mapToLong { it.time }.sum()
            for (data in pieDataList) {
                data.timeRatio = data.time / totalTime.toFloat()
                LogUtils.d("${data.time}   ${data.orgName}  ${data.timeRatio} ")
                pieEntries.add(PieEntry(data.timeRatio, data.orgName))
            }
            val pieSet = PieDataSet(pieEntries, "机构")
            pieSet.setColors(*ColorPalette.Primary)
            val pieData = PieData(pieSet)
            binding.pieChart.data = pieData
            binding.pieChart.invalidate() // refresh
        }
        pieItemListAdapter.setNewInstance(pieDataList?.toMutableList())
    }

    @SuppressLint("SetTextI18n")
    override fun getStatisticOverviewResult(statisticOverview: StatisticOverview?) {
        if (statisticOverview == null)
            return
        binding.mineStatisticReservationTimes.text = statisticOverview.reservationTimes.toString()
        binding.mineStatisticBreachTimes.text = statisticOverview.breachTimes.toString()
        binding.mineStatisticStudyTime.text = "${statisticOverview.studyTime / 1000 / 60 / 60}时${statisticOverview.studyTime / 1000 / 60 % 60}分"
        binding.mineStatisticStudyTimePerDay.text = "${statisticOverview.studyTimeAverage / 1000 / 60 / 60}时${statisticOverview.studyTimeAverage / 1000 / 60 % 60}分"
    }
}