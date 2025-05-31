package com.himanshuhc.jsontoui.ui

sealed class UiComponent {
    abstract val id: String?

    data class TextComponent(
        override val id: String,
        val style: String?
    ) : UiComponent()

    data class ImageComponent(
        override val id: String
    ) : UiComponent()

    data class ButtonComponent(
        override val id: String
    ) : UiComponent()

    data class SpacerComponent(
        override val id: String
    ) : UiComponent()

    data class InputComponent(
        override val id: String
    ) : UiComponent()

    data class RowComponent(
        val children: List<UiComponent>,
        val style: String?
    ) : UiComponent() {
        override val id: String? = null
    }

    data class ColumnComponent(
        val children: List<UiComponent>,
        val style: String?
    ) : UiComponent() {
        override val id: String? = null
    }

    data class UnknownComponent(
        override val id: String?,
        val type: String
    ) : UiComponent()
}
