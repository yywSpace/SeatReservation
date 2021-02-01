package com.yywspace.module_mine.user

import android.os.Bundle
import android.text.InputType
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.google.android.material.textfield.TextInputLayout
import com.yywspace.module_base.bean.User
import com.yywspace.module_mine.R
import com.yywspace.module_mine.databinding.MineUserInfoActivityBinding

class InfoDetailActivity : AppCompatActivity() {

    private lateinit var binding: MineUserInfoActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MineUserInfoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true); //设置返回键可用
        initView()
    }

    private fun initView() {
        val user = intent.getParcelableExtra<User>("user")
        binding.infoMessage.text = user?.message
        binding.infoSex.text = when (user?.sex) {
            0 -> "女"
            1 -> "男"
            else -> "未定义"
        }
        binding.infoName.text = user?.username
        if (user?.message == null || user.message == "") {
            binding.infoMessage.text = getString(R.string.mine_user_desc_default)
        } else {
            binding.infoMessage.text = user.message
        }
        binding.apply {
            infoAvatarItem.setOnClickListener {

            }
            infoMessageItem.setOnClickListener {
                MaterialDialog(this@InfoDetailActivity).show {
                    title(text = "个人留言")
                    customView(R.layout.mine_user_info_item_message_dialog)
                    val inputLayout = getCustomView().findViewById<TextInputLayout>(R.id.info_message_input_layout)
                    if (user?.message == null || user.message == "") {
                        inputLayout.hint = getString(R.string.mine_user_desc_default)
                        binding.infoMessage.text = getString(R.string.mine_user_desc_default)
                    } else {
                        inputLayout.hint = user.message
                        binding.infoMessage.text = user.message
                    }

                    positiveButton(R.string.base_dialog_confirm) {
                        user?.message = inputLayout.editText?.text.toString()
                        binding.infoMessage.text = user?.message
                    }
                    negativeButton(R.string.base_dialog_cancel)
                }
            }
            infoSexItem.setOnClickListener {
                MaterialDialog(this@InfoDetailActivity).show {
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
                    }
                    negativeButton(R.string.base_dialog_cancel)
                }
            }
            infoNameItem.setOnClickListener {
                MaterialDialog(this@InfoDetailActivity).show {
                    title(text = "修改昵称")
                    input(waitForPositiveButton = false, hint = user?.username, allowEmpty = false) { dialog, text ->
                        user?.username = text.toString()
                        binding.infoName.text = text.toString()
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
}