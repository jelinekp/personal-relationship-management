package cz.wz.jelinekp.prm.features.contacts.model

sealed class Categories(private val nameValue: String, val types: CategoryTypes) {
    object Family: Categories("family", CategoryTypes.PRESET)
    object Friends: Categories("friends", CategoryTypes.PRESET)
    object Other: Categories("other", CategoryTypes.PRESET)
    data class Custom(val customName: String): Categories("custom", CategoryTypes.CUSTOM)

    override fun toString(): String {
        return nameValue
    }
}

enum class CategoryTypes {
    PRESET, CUSTOM
}