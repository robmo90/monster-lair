package de.enduni.monsterlair.common

import android.text.Editable
import com.google.android.material.textfield.TextInputEditText

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