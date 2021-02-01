package com.yywspace.module_base.contract

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

public class PhotoSelectResultContract : ActivityResultContract<Void?, Bitmap>() {
    lateinit var context: Context
    override fun createIntent(context: Context, input: Void?): Intent {
        this.context = context
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        return intentToPickPic
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bitmap? {
        if (intent != null && intent.data != null) {
            val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(intent.data!!))
            return bitmap;
        }
        return null
    }
}