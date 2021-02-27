package com.yywspace.module_mine.user.activity

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.input
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.yywspace.module_base.AppConfig
import com.yywspace.module_base.base.BaseActivity
import com.yywspace.module_base.base.BaseResponse
import com.yywspace.module_base.bean.User
import com.yywspace.module_mine.R
import com.yywspace.module_mine.databinding.MineUserInfoActivityBinding
import com.yywspace.module_mine.iview.IUserInfoDetailView
import com.yywspace.module_mine.presenter.UserInfoDetailPresenter
import com.yywspace.module_base.GlideEngine
import com.yywspace.module_base.util.LogUtils


class UserInfoDetailActivity : BaseActivity<IUserInfoDetailView, UserInfoDetailPresenter>(), IUserInfoDetailView {
    var user: User? = null
    private lateinit var binding: MineUserInfoActivityBinding


    private fun initView() {
        user = intent.getParcelableExtra("user")

        binding.infoMessage.text = user?.message
        binding.infoSex.text = when (user?.sex) {
            0 -> "女"
            1 -> "男"
            else -> "未定义"
        }
        Glide.with(baseContext)
                .load(AppConfig.BASE_URL + "upload/" + user?.avatarPath)
                .placeholder(R.drawable.ic_avatar)//图片加载出来前，显示的图片
                .error(R.drawable.ic_avatar)//图片加载失败后，显示的图片
                .into(binding.infoAvatar)
        binding.infoName.text = user?.username
        if (user?.message == null || user?.message == "") {
            binding.infoMessage.text = getString(R.string.mine_user_desc_default)
        } else {
            binding.infoMessage.text = user?.message
        }
        binding.apply {
            infoAvatarItem.setOnClickListener {
                PictureSelector.create(this@UserInfoDetailActivity)
                        .openGallery(PictureMimeType.ofAll())
                        .selectionMode(PictureConfig.SINGLE)
                        .isEnableCrop(true)
                        .cropImageWideHigh(200, 200)
                        .withAspectRatio(1, 1)//裁剪比例
                        .rotateEnabled(false)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                            override fun onResult(result: List<LocalMedia?>) {
                                // 结果回调
                                if (result.isEmpty())
                                    return
                                val media = result[0]
                                val path = if (media?.isCut == true) media.cutPath else media?.path
                                Glide.with(baseContext)
                                        .load(path)
                                        .placeholder(R.drawable.ic_avatar)//图片加载出来前，显示的图片
                                        .error(R.drawable.ic_avatar)//图片加载失败后，显示的图片
                                        .into(binding.infoAvatar)
                                presenter.uploadFile(this@UserInfoDetailActivity, path!!)
                            }

                            override fun onCancel() {
                                // 取消
                            }
                        })
            }
            infoMessageItem.setOnClickListener {
                MaterialDialog(this@UserInfoDetailActivity).show {
                    title(text = "个人留言")
                    customView(R.layout.mine_user_info_item_message_dialog)
                    val inputLayout = getCustomView().findViewById<TextInputLayout>(R.id.info_message_input_layout)
                    if (user?.message == null || user?.message == "") {
                        inputLayout.hint = getString(R.string.mine_user_desc_default)
                        binding.infoMessage.text = getString(R.string.mine_user_desc_default)
                    } else {
                        inputLayout.hint = user?.message
                        binding.infoMessage.text = user?.message
                    }

                    positiveButton(R.string.base_dialog_confirm) {
                        user?.message = inputLayout.editText?.text.toString()
                        binding.infoMessage.text = user?.message
                        setResultForParent()
                    }
                    negativeButton(R.string.base_dialog_cancel)
                }
            }
            infoSexItem.setOnClickListener {
                MaterialDialog(this@UserInfoDetailActivity).show {
                    title(text = "选择性别")
                    customView(R.layout.mine_user_info_item_sex_dialog)
                    val customView = getCustomView()
                    val sexWoman = customView.findViewById<RadioButton>(R.id.radio_woman)
                    val sexMan = customView.findViewById<RadioButton>(R.id.radio_man)
                    when (user?.sex) {
                        0 -> sexWoman.isChecked = true
                        1 -> sexMan.isChecked = true
                        else -> {
                        }
                    }
                    positiveButton(R.string.base_dialog_confirm) {
                        if (sexWoman.isChecked) user?.sex = 0
                        else user?.sex = 1
                        binding.infoSex.text = when (user?.sex) {
                            0 -> "女"
                            1 -> "男"
                            else -> "未定义"
                        }
                        setResultForParent()
                    }
                    negativeButton(R.string.base_dialog_cancel)
                }
            }
            infoNameItem.setOnClickListener {
                MaterialDialog(this@UserInfoDetailActivity).show {
                    title(text = "修改昵称")
                    input(waitForPositiveButton = false, hint = user?.username, allowEmpty = false) { dialog, text ->
                        user?.username = text.toString()
                        binding.infoName.text = text.toString()
                        setResultForParent()
                    }
                    positiveButton(R.string.base_dialog_confirm)
                    negativeButton(R.string.base_dialog_cancel)
                }
            }
            infoPasswordItem.setOnClickListener {

            }
            infoLogoutItem.setOnClickListener {

            }
        }
    }

    private fun setResultForParent() {
        User.currentUser = user
        presenter.updateUserInfo(this, user)
        val intent = Intent().apply {
            putExtra("user", user)
        }
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent().apply {
                    putExtra("user", user)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            else -> {
                true
            }
        }
    }

    override fun getLayout(inflater: LayoutInflater): View {
        binding = MineUserInfoActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createPresenter(): UserInfoDetailPresenter {
        return UserInfoDetailPresenter()
    }

    override fun createView(): IUserInfoDetailView {
        return this
    }

    override fun init() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        initView()
    }

    override fun updateUserInfoResult(response: BaseResponse<Any>) {
        LogUtils.d(response.toString())
        Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
    }

    override fun uploadFileResult(response: BaseResponse<String>) {
        LogUtils.d(response.toString())
        if (response.code == 1) {
            user?.avatarPath = response.data
            setResultForParent()
        }
        else
            Toast.makeText(this, "图像上传失败", Toast.LENGTH_SHORT).show();
    }
}