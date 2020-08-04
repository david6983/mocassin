package com.david.mocassin.utils.lang

import java.util.*

enum class SupportedLocale(val local: Locale) {
    ENGLISH(Locale.ENGLISH),
    FRENCH(Locale.FRENCH),
    CHINESE(Locale.CHINESE);

    companion object {
        val supportedLocals: List<SupportedLocale>
            get() = SupportedLocale.values().toList()

        fun isSupportedLocal(local: Locale): SupportedLocale? {
            supportedLocals.forEach {
                if (it.local == local) {
                    return it
                }
            }

            return null
        }
    }
}