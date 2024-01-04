package com.github.alexzhirkevich.customqrgenerator

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.github.alexzhirkevich.customqrgenerator.dsl.QrOptionsBuilderScope
import com.github.alexzhirkevich.customqrgenerator.style.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * @param padding padding of the QR code relative to [width] and [height].
 * */
@Serializable
data class QrOptions(
    @IntRange(from = 0) val width : Int,
    @IntRange(from = 0) val height : Int,
    @FloatRange(from = .0, to = .5) val padding : Float,
    val offset: QrOffset,
    val colors : QrColors,
    val logo: QrLogo,
    val background: QrBackground,
    val shapes: QrElementsShapes,
    val codeShape : QrShape,
    val errorCorrectionLevel: QrErrorCorrectionLevel
) {

    class Builder(
        @IntRange(from = 0) val width: Int,
        @IntRange(from = 0) val height: Int = width
    ) {

        var padding = .125f
        var offset = QrOffset.Zero
        var colors = QrColors()
        var logo = QrLogo()
        var background = QrBackground()
        var elementsShapes = QrElementsShapes()
        var codeShape: QrShape = QrShape.Default
        var errorCorrectionLevel: QrErrorCorrectionLevel = QrErrorCorrectionLevel.Auto

        fun build(): QrOptions = QrOptions(
            width,height, padding, offset, colors, logo, background,
            elementsShapes, codeShape, errorCorrectionLevel
        )

        /**
         * Padding of the QR code relative to [width] and [height].
         * */
        fun setPadding(@FloatRange(from = 0.0, to = .5) padding: Float) = apply {
            this.padding = padding
        }

        fun setOffset(offset: QrOffset) = apply {
            this.offset = offset
        }

        fun setColors(colors: QrColors) = apply {
            this.colors = colors
        }

        fun setLogo(logo: QrLogo?) = apply {
            this.logo = logo ?: QrLogo()
        }

        fun setBackground(background: QrBackground?) = apply {
            this.background = background ?: QrBackground()
        }

        fun setCodeShape(shape: QrShape): Builder = apply {
            this.codeShape = shape
        }

        fun setElementsShapes(shapes: QrElementsShapes) = apply {
            this.elementsShapes = shapes
        }

        fun setErrorCorrectionLevel(level: QrErrorCorrectionLevel) = apply {
            errorCorrectionLevel = level
        }
    }


    companion object : SerializationProvider {

        @ExperimentalSerializationApi
        override val defaultSerializersModule by lazy(LazyThreadSafetyMode.NONE) {
            SerializersModuleFromProviders(
                QrColors,
                QrLogo,
                QrBackground,
                QrElementsShapes,
                QrShape
            )
        }
    }
}
/**
 * Build [QrOptions] with DSL
 * */
inline fun createQrOptions(
    width: Int,
    height: Int = width,
    padding: Float = .125f,
    crossinline build : QrOptionsBuilderScope.() -> Unit
) : QrOptions = with(QrOptions.Builder(width, height).setPadding(padding)) {
    QrOptionsBuilderScope(this).apply(build)
    build()
}
