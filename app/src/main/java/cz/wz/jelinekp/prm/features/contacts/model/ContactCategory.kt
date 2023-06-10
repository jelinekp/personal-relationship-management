package cz.wz.jelinekp.prm.features.contacts.model

import cz.wz.jelinekp.prm.R

sealed class ContactCategory(
    private val nameValue: String,
    val types: CategoryTypes,
    val stringResource: Int,
) {
    object Family : ContactCategory("family", CategoryTypes.PRESET, R.string.category_family)
    object Friends : ContactCategory("friends", CategoryTypes.PRESET, R.string.category_friends)
    object Other : ContactCategory("other", CategoryTypes.PRESET, R.string.category_other)
    data class Custom(val customName: String) : ContactCategory("custom", CategoryTypes.CUSTOM, R.string.category_custom) {
        fun equals(other: ContactCategory.Custom): Boolean {
            return this.customName == other.customName
        }
    }

    override fun toString(): String {
        return nameValue
    }
}

enum class CategoryTypes {
    PRESET, CUSTOM
}