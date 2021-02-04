package com.yywspace.module_mine.user.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
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
import com.yywspace.module_base.bean.User
import com.yywspace.module_mine.R
import com.yywspace.module_mine.databinding.MineUserInfoActivityBinding
import com.yywspace.module_mine.user.GlideEngine


class UserInfoDetailActivity : AppCompatActivity() {
    var user: User? = null
    private lateinit var binding: MineUserInfoActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MineUserInfoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        initView()
    }

    private fun initView() {
        user = intent.getParcelableExtra("user")

        binding.infoMessage.text = user?.message
        binding.infoSex.text = when (user?.sex) {
            0 -> "女"
            1 -> "男"
            else -> "未定义"
        }
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
                                // TODO: 21-2-1
                                user?.avatar = path
                                setResultForParent()
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

    fun setResultForParent() {
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
}