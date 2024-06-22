package io.nekohasekai.sfa.constant

import io.nekohasekai.sfa.R
import android.content.Context

enum class EnabledType(val boolValue: Boolean, val stringResId: Int) {
    Enabled(true, R.string.enabled),
    Disabled(false, R.string.disabled);

    companion object {
        fun from(value: Boolean): EnabledType {
            return if (value) Enabled else Disabled
        }

        fun fromLocalizedString(context: Context, value: String): EnabledType {
            return values().find { it.getLocalizedString(context) == value }
                ?: throw IllegalArgumentException("Invalid EnabledType value: $value")
        }
    }

    fun getLocalizedString(context: Context): String {
        return context.getString(stringResId)
    }
}
