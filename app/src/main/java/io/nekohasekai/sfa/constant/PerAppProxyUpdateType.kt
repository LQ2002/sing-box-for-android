package io.nekohasekai.sfa.constant

import io.nekohasekai.sfa.database.Settings
import android.content.Context
import io.nekohasekai.sfa.R

enum class PerAppProxyUpdateType(val displayNameResId: Int) {
    Disabled(R.string.disabled),
    Select(R.string.select),
    Deselect(R.string.deselect);

    fun value() = when (this) {
        Disabled -> Settings.PER_APP_PROXY_DISABLED
        Select -> Settings.PER_APP_PROXY_INCLUDE
        Deselect -> Settings.PER_APP_PROXY_EXCLUDE
    }

    fun getLocalizedDisplayName(context: Context): String {
        return context.getString(displayNameResId)
    }

    companion object {
        fun valueOf(value: Int): PerAppProxyUpdateType = when (value) {
            Settings.PER_APP_PROXY_DISABLED -> Disabled
            Settings.PER_APP_PROXY_INCLUDE -> Select
            Settings.PER_APP_PROXY_EXCLUDE -> Deselect
            else -> throw IllegalArgumentException("Invalid value: $value")
        }

        fun fromDisplayName(context: Context, displayName: String): PerAppProxyUpdateType {
            return values().find { context.getString(it.displayNameResId) == displayName }
                ?: throw IllegalArgumentException("Unknown display name: $displayName")
        }
    }
}
