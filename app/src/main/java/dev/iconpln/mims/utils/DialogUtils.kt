package dev.iconpln.mims.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype
import dev.iconpln.mims.R

object DialogUtils {
    fun getDialog(context: Context, messsage: String): NiftyDialogBuilder {
        return NiftyDialogBuilder.getInstance(context)
            .withTitle(context.getString(R.string.label_confirm))
            .withMessage(messsage)
            .withMessageColor("#FFFFFF")
            .withDialogColor(String.format("#FF%06x", ContextCompat.getColor(context, R.color.blue_light) and 0xffffff))
            .withIcon(ContextCompat.getDrawable(context, R.drawable.img_question)).withDuration(500)
            .withEffect(Effectstype.Slidetop).withButton1Text(context.getString(R.string.label_no).toUpperCase())
            .withButton2Text(context.getString(R.string.label_yes).toUpperCase())
//            .withButtonDrawable(R.drawable.button_rounded_style_primary_dark)
            .isCancelableOnTouchOutside(false)
    }

    fun getDialogFocus(context: Context, messsage: String): NiftyDialogBuilder {
        return NiftyDialogBuilder.getInstance(context)
            .withTitle(context.getString(R.string.label_ask_store))
            .withMessage(messsage)
            .withMessageColor("#FFDFDFDF")
            .withDialogColor(String.format("#FF%06x", ContextCompat.getColor(context, R.color.colorPrimary) and 0xffffff))
            .withIcon(ContextCompat.getDrawable(context, R.drawable.img_question)).withDuration(500)
            .withEffect(Effectstype.Slidetop).withButton1Text(context.getString(R.string.label_no).toUpperCase())
            .withButton2Text(context.getString(R.string.label_yes).toUpperCase())
//            .withButtonDrawable(R.drawable.button_rounded_style_primary_dark)
            .isCancelableOnTouchOutside(false)
    }

    fun getLogoutDialog(context: Context): NiftyDialogBuilder {
        return NiftyDialogBuilder.getInstance(context)
            .withTitle(context.getString(R.string.label_confirm))
            .withMessage(context.getString(R.string.confirm_msg_logout))
            .withMessageColor("#FFFFFF")
            .withDialogColor(String.format("#FF%06x", ContextCompat.getColor(context, R.color.blue_light) and 0xffffff))
            .withIcon(ContextCompat.getDrawable(context, R.drawable.img_question)).withDuration(500)
            .withEffect(Effectstype.Slidetop).withButton1Text(context.getString(R.string.label_no).toUpperCase())
            .withButton2Text(context.getString(R.string.label_logout).toUpperCase())
//            .withButtonDrawable(R.drawable.button_rounded_style_primary_dark)
            .isCancelableOnTouchOutside(false)
    }

    fun getAbsenDialog(context: Context, messsage: String): NiftyDialogBuilder {
        return NiftyDialogBuilder.getInstance(context)
            .withTitle(context.getString(R.string.label_confirm))
            .withMessage(messsage)
            .withMessageColor("#FFDFDFDF")
            .withDialogColor(String.format("#FF%06x", ContextCompat.getColor(context, R.color.colorPrimary) and 0xffffff))
            .withIcon(ContextCompat.getDrawable(context, R.drawable.img_question)).withDuration(500)
            .withEffect(Effectstype.Slidetop).withButton1Text(context.getString(R.string.label_no).toUpperCase())
            .withButton2Text(context.getString(R.string.label_yes).toUpperCase())
//            .withButtonDrawable(R.drawable.button_rounded_style_primary_dark)
            .isCancelableOnTouchOutside(false)
    }

    fun getDialogInformation(context: Context, messsage: String): NiftyDialogBuilder {
        return NiftyDialogBuilder.getInstance(context)
            .withTitle(context.getString(R.string.label_perhatian))
            .withMessage(messsage)
            .withMessageColor("#FFDFDFDF")
            .withDialogColor(String.format("#FF%06x", ContextCompat.getColor(context, R.color.colorPrimary) and 0xffffff))
            .withIcon(ContextCompat.getDrawable(context, R.drawable.img_question)).withDuration(500)
            .withEffect(Effectstype.Slidetop)
            .withButton2Text(context.getString(R.string.label_selesai).toUpperCase())
//            .withButtonDrawable(R.drawable.button_rounded_style_primary_dark)
            .isCancelableOnTouchOutside(false)
    }
}
