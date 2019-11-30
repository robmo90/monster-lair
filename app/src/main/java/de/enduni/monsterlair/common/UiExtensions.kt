package de.enduni.monsterlair.common

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.AutoCompleteTextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import de.enduni.monsterlair.R

fun AutoCompleteTextView.setTextIfNotFocused(string: String?) {
    if (!this.isFocused) {
        if (!string.isNullOrEmpty()) {
            this.text = Editable.Factory.getInstance().newEditable(string)
        }
    }
}

fun TextInputEditText.setTextIfNotFocused(string: String?) {
    if (!this.isFocused) {
        if (!string.isNullOrEmpty()) {
            this.text = Editable.Factory.getInstance().newEditable(string)
        }
    }
}

fun TextInputEditText.setTextIfNotFocused(int: Int?) {
    if (!this.isFocused) {
        if (int != null) {
            this.text = Editable.Factory.getInstance().newEditable(int.toString())
        }
    }
}

fun Uri.openCustomTab(context: Context) {
    CustomTabsIntent.Builder()
        .setToolbarColor(ContextCompat.getColor(context, R.color.primaryColor))
        .addDefaultShareMenuItem()
        .setShowTitle(true)
        .setStartAnimations(
            context,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        .setExitAnimations(
            context,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .build()
        .launchUrl(context, this)
}

fun Int.toDp(displayMetrics: DisplayMetrics) = toFloat().toDp(displayMetrics).toInt()

fun Float.toDp(displayMetrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)