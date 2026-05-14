package com.nammapusthakaa.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin

private val SkinColor = Color(0xFFFDDCB5)
private val SkinShadow = Color(0xFFF0C899)
private val HairColor = Color(0xFF3D2B1F)
private val UniformColor = Color(0xFF1565C0)
private val UniformDark = Color(0xFF0D47A1)
private val SkirtColor = Color(0xFF558B2F)
private val BookRed = Color(0xFFE53935)
private val BookBlue = Color(0xFF1E88E5)
private val BookGreen = Color(0xFF43A047)
private val BookYellow = Color(0xFFFDD835)
private val SparkleColor = Color(0xFFFFD600)
private val GrassGreen = Color(0xFF66BB6A)
private val GrassLight = Color(0xFFA5D6A7)
private val SunColor = Color(0xFFFFF176)
private val SkyBlue = Color(0xFF90CAF9)
private val CloudWhite = Color(0xFFFFFFFF).copy(alpha = 0.7f)
private val ShirtColor = Color(0xFF42A5F5)
private val TrouserColor = Color(0xFF37474F)
private val ShoeColor = Color(0xFF5D4037)

@Composable
fun ExcitedStudentFull(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f

        drawSky(w, h)
        drawSun(w * 0.85f, h * 0.15f, w * 0.08f)
        drawCloud(w * 0.1f, h * 0.12f, w * 0.18f)
        drawCloud(w * 0.55f, h * 0.08f, w * 0.14f)
        drawHills(w, h)
        drawGrass(w, h)
        drawSparkles(cx, h * 0.25f, w * 0.35f)
        drawExcitedStudent(cx, h * 0.55f, w * 0.14f)
    }
}

@Composable
fun HappyStudentsGroup(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawSky(w, h)
        drawSun(w * 0.15f, h * 0.12f, w * 0.06f)
        drawCloud(w * 0.6f, h * 0.1f, w * 0.15f)
        drawHills(w, h)
        drawGrass(w, h)
        drawSparkles(w * 0.5f, h * 0.2f, w * 0.4f)
        drawExcitedStudent(w * 0.3f, h * 0.58f, w * 0.1f)
        drawReadingStudent(w * 0.55f, h * 0.6f, w * 0.09f)
        drawJumpingStudent(w * 0.78f, h * 0.55f, w * 0.08f)
    }
}

@Composable
fun HappyReader(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawReadingStudent(w * 0.5f, h * 0.55f, w * 0.18f)
    }
}

@Composable
fun CheerfulEmptyState(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f

        drawSky(w, h)
        drawHills(w, h)
        drawGrass(w, h)
        drawSparkles(cx, h * 0.25f, w * 0.3f)
        drawExcitedStudent(cx, h * 0.55f, w * 0.12f)
    }
}

private fun DrawScope.drawSky(w: Float, h: Float) {
    drawRect(
        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
            listOf(SkyBlue.copy(alpha = 0.4f), Color.White.copy(alpha = 0.8f))
        ),
        size = Size(w, h * 0.7f)
    )
}

private fun DrawScope.drawSun(x: Float, y: Float, r: Float) {
    drawCircle(color = SunColor.copy(alpha = 0.8f), radius = r, center = Offset(x, y))
    for (angle in 0 until 360 step 30) {
        val radians = Math.toRadians(angle.toDouble())
        val startX = x + r * 0.85f * cos(radians).toFloat()
        val startY = y + r * 0.85f * sin(radians).toFloat()
        val endX = x + r * 1.4f * cos(radians).toFloat()
        val endY = y + r * 1.4f * sin(radians).toFloat()
        drawLine(
            color = SunColor.copy(alpha = 0.6f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = r * 0.12f,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawCloud(x: Float, y: Float, w: Float) {
    val r = w * 0.3f
    drawCircle(color = CloudWhite, radius = r, center = Offset(x, y))
    drawCircle(color = CloudWhite, radius = r * 0.8f, center = Offset(x + r * 0.8f, y - r * 0.15f))
    drawCircle(color = CloudWhite, radius = r * 0.7f, center = Offset(x - r * 0.7f, y + r * 0.1f))
    drawCircle(color = CloudWhite, radius = r * 0.6f, center = Offset(x + r * 1.4f, y + r * 0.05f))
}

private fun DrawScope.drawHills(w: Float, h: Float) {
    val path = Path()
    path.moveTo(0f, h * 0.65f)
    path.quadraticBezierTo(w * 0.15f, h * 0.5f, w * 0.3f, h * 0.62f)
    path.quadraticBezierTo(w * 0.5f, h * 0.5f, w * 0.7f, h * 0.6f)
    path.quadraticBezierTo(w * 0.85f, h * 0.52f, w, h * 0.58f)
    path.lineTo(w, h)
    path.lineTo(0f, h)
    path.close()
    drawPath(path, color = GrassGreen.copy(alpha = 0.3f))
}

private fun DrawScope.drawGrass(w: Float, h: Float) {
    val path = Path()
    path.moveTo(0f, h * 0.72f)
    path.quadraticBezierTo(w * 0.25f, h * 0.7f, w * 0.5f, h * 0.73f)
    path.quadraticBezierTo(w * 0.75f, h * 0.7f, w, h * 0.72f)
    path.lineTo(w, h)
    path.lineTo(0f, h)
    path.close()
    drawPath(path, color = GrassLight.copy(alpha = 0.5f))

    for (i in 0 until 12) {
        val gx = w * (0.05f + 0.08f * i)
        val gy = h * (0.7f + 0.02f * (i % 3))
        drawLine(
            color = GrassGreen.copy(alpha = 0.4f),
            start = Offset(gx, gy),
            end = Offset(gx - w * 0.01f, gy - h * 0.03f),
            strokeWidth = w * 0.005f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = GrassGreen.copy(alpha = 0.4f),
            start = Offset(gx, gy),
            end = Offset(gx + w * 0.01f, gy - h * 0.025f),
            strokeWidth = w * 0.005f,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawSparkles(cx: Float, cy: Float, spread: Float) {
    val sparklePositions = listOf(
        Offset(cx - spread * 0.6f, cy - spread * 0.3f),
        Offset(cx + spread * 0.5f, cy - spread * 0.4f),
        Offset(cx - spread * 0.3f, cy - spread * 0.6f),
        Offset(cx + spread * 0.7f, cy + spread * 0.1f),
        Offset(cx - spread * 0.8f, cy + spread * 0.2f),
        Offset(cx + spread * 0.2f, cy - spread * 0.7f),
    )
    for (pos in sparklePositions) {
        drawStar(pos, spread * 0.06f, SparkleColor)
    }
}

private fun DrawScope.drawStar(center: Offset, size: Float, color: Color) {
    val path = Path()
    val points = 5
    for (i in 0 until points * 2) {
        val angle = Math.toRadians((i * 360.0 / (points * 2)) - 90.0)
        val r = if (i % 2 == 0) size else size * 0.4f
        val x = center.x + r * cos(angle).toFloat()
        val y = center.y + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color, style = Fill)
}

private fun DrawScope.drawExcitedStudent(cx: Float, groundY: Float, scale: Float) {
    val s = scale
    val headR = s * 0.55f
    val headY = groundY - s * 2.5f

    drawCircle(color = SkinColor, radius = headR, center = Offset(cx, headY))
    drawHair(cx, headY, headR)

    val eyeY = headY - headR * 0.05f
    drawCircle(color = Color(0xFF2C1810), radius = s * 0.06f, center = Offset(cx - headR * 0.3f, eyeY))
    drawCircle(color = Color(0xFF2C1810), radius = s * 0.06f, center = Offset(cx + headR * 0.3f, eyeY))
    drawCircle(color = Color.White, radius = s * 0.025f, center = Offset(cx - headR * 0.25f, eyeY - s * 0.02f))
    drawCircle(color = Color.White, radius = s * 0.025f, center = Offset(cx + headR * 0.35f, eyeY - s * 0.02f))

    drawArc(
        color = Color(0xFFD84315),
        startAngle = 10f,
        sweepAngle = 160f,
        useCenter = false,
        topLeft = Offset(cx - headR * 0.3f, headY + headR * 0.1f),
        size = Size(headR * 0.6f, headR * 0.35f),
        style = Stroke(width = s * 0.06f, cap = StrokeCap.Round)
    )

    drawCheeks(cx, headY, headR)

    val bodyTopY = headY + headR * 0.6f
    val bodyH = s * 1.2f

    val bodyPath = Path().apply {
        moveTo(cx - s * 0.5f, bodyTopY)
        quadraticBezierTo(cx - s * 0.3f, bodyTopY + s * 0.1f, cx, bodyTopY)
        quadraticBezierTo(cx + s * 0.3f, bodyTopY + s * 0.1f, cx + s * 0.5f, bodyTopY)
        lineTo(cx + s * 0.6f, bodyTopY + bodyH)
        lineTo(cx - s * 0.6f, bodyTopY + bodyH)
        close()
    }
    drawPath(bodyPath, color = UniformColor)

    val collarPath = Path().apply {
        moveTo(cx - s * 0.25f, bodyTopY)
        lineTo(cx, bodyTopY + s * 0.25f)
        lineTo(cx + s * 0.25f, bodyTopY)
    }
    drawPath(collarPath, color = UniformDark, style = Stroke(width = s * 0.06f, cap = StrokeCap.Round))

    drawLine(
        color = UniformDark,
        start = Offset(cx, bodyTopY + s * 0.25f),
        end = Offset(cx, bodyTopY + bodyH * 0.4f),
        strokeWidth = s * 0.04f
    )

    val armPathL = Path().apply {
        moveTo(cx - s * 0.55f, bodyTopY + s * 0.3f)
        quadraticBezierTo(cx - s * 1.3f, bodyTopY - s * 0.2f, cx - s * 1.1f, bodyTopY - s * 0.8f)
    }
    drawPath(armPathL, color = SkinColor, style = Stroke(width = s * 0.18f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    val armPathR = Path().apply {
        moveTo(cx + s * 0.55f, bodyTopY + s * 0.3f)
        quadraticBezierTo(cx + s * 1.3f, bodyTopY - s * 0.2f, cx + s * 1.1f, bodyTopY - s * 0.6f)
    }
    drawPath(armPathR, color = SkinColor, style = Stroke(width = s * 0.18f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    val bookW = s * 0.5f
    val bookH = s * 0.35f
    val bookX = cx + s * 0.85f
    val bookY = bodyTopY - s * 0.75f
    drawRoundRect(color = BookRed, topLeft = Offset(bookX, bookY), size = Size(bookW, bookH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.04f))
    drawLine(color = Color.White.copy(alpha = 0.5f), start = Offset(bookX + bookW * 0.2f, bookY), end = Offset(bookX + bookW * 0.2f, bookY + bookH), strokeWidth = s * 0.02f)
    drawLine(color = Color.White.copy(alpha = 0.5f), start = Offset(bookX + bookW * 0.5f, bookY), end = Offset(bookX + bookW * 0.5f, bookY + bookH), strokeWidth = s * 0.02f)
    drawLine(color = Color.White.copy(alpha = 0.5f), start = Offset(bookX + bookW * 0.8f, bookY), end = Offset(bookX + bookW * 0.8f, bookY + bookH), strokeWidth = s * 0.02f)

    val legStartY = bodyTopY + bodyH
    val legPathL = Path().apply {
        moveTo(cx - s * 0.25f, legStartY)
        quadraticBezierTo(cx - s * 0.4f, legStartY + s * 0.6f, cx - s * 0.5f, legStartY + s * 0.8f)
    }
    drawPath(legPathL, color = TrouserColor, style = Stroke(width = s * 0.2f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    val legPathR = Path().apply {
        moveTo(cx + s * 0.25f, legStartY)
        quadraticBezierTo(cx + s * 0.6f, legStartY + s * 0.2f, cx + s * 0.7f, legStartY + s * 0.5f)
    }
    drawPath(legPathR, color = TrouserColor, style = Stroke(width = s * 0.2f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    drawCircle(color = ShoeColor, radius = s * 0.12f, center = Offset(cx - s * 0.5f, legStartY + s * 0.8f))
    drawCircle(color = ShoeColor, radius = s * 0.12f, center = Offset(cx + s * 0.7f, legStartY + s * 0.5f))
}

private fun DrawScope.drawReadingStudent(cx: Float, groundY: Float, scale: Float) {
    val s = scale
    val headR = s * 0.55f
    val headY = groundY - s * 1.8f

    drawCircle(color = SkinColor, radius = headR, center = Offset(cx, headY))
    drawHair(cx, headY, headR)

    val eyeY = headY - headR * 0.05f
    drawArc(
        color = Color(0xFF2C1810),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx - headR * 0.4f, eyeY - s * 0.02f),
        size = Size(headR * 0.2f, headR * 0.15f),
        style = Stroke(width = s * 0.05f)
    )
    drawArc(
        color = Color(0xFF2C1810),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx + headR * 0.2f, eyeY - s * 0.02f),
        size = Size(headR * 0.2f, headR * 0.15f),
        style = Stroke(width = s * 0.05f)
    )

    drawArc(
        color = Color(0xFFD84315),
        startAngle = 10f,
        sweepAngle = 160f,
        useCenter = false,
        topLeft = Offset(cx - headR * 0.3f, headY + headR * 0.15f),
        size = Size(headR * 0.6f, headR * 0.3f),
        style = Stroke(width = s * 0.05f, cap = StrokeCap.Round)
    )

    drawCheeks(cx, headY, headR)

    val bodyTopY = headY + headR * 0.6f
    val bodyH = s * 1.0f

    val bodyPath = Path().apply {
        moveTo(cx - s * 0.45f, bodyTopY)
        quadraticBezierTo(cx, bodyTopY + s * 0.1f, cx + s * 0.45f, bodyTopY)
        lineTo(cx + s * 0.5f, bodyTopY + bodyH)
        lineTo(cx - s * 0.5f, bodyTopY + bodyH)
        close()
    }
    drawPath(bodyPath, color = Color(0xFFEF5350))

    drawLine(color = SkinColor, start = Offset(cx - s * 0.48f, bodyTopY + s * 0.4f), end = Offset(cx - s * 0.5f, bodyTopY + bodyH * 0.8f), strokeWidth = s * 0.15f, cap = StrokeCap.Round)
    drawLine(color = SkinColor, start = Offset(cx + s * 0.48f, bodyTopY + s * 0.4f), end = Offset(cx + s * 0.5f, bodyTopY + bodyH * 0.8f), strokeWidth = s * 0.15f, cap = StrokeCap.Round)

    val bookW = s * 0.7f
    val bookH = s * 0.45f
    val bookX = cx - bookW / 2f
    val bookY = bodyTopY + bodyH * 0.2f
    drawRoundRect(color = BookBlue, topLeft = Offset(bookX, bookY), size = Size(bookW, bookH), cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.05f))
    drawLine(color = Color.White.copy(alpha = 0.6f), start = Offset(cx, bookY), end = Offset(cx, bookY + bookH), strokeWidth = s * 0.03f)
    drawLine(color = Color.White.copy(alpha = 0.4f), start = Offset(bookX + bookW * 0.3f, bookY), end = Offset(bookX + bookW * 0.3f, bookY + bookH), strokeWidth = s * 0.02f)
    drawLine(color = Color.White.copy(alpha = 0.4f), start = Offset(bookX + bookW * 0.7f, bookY), end = Offset(bookX + bookW * 0.7f, bookY + bookH), strokeWidth = s * 0.02f)

    val legPath = Path().apply {
        moveTo(cx - s * 0.2f, bodyTopY + bodyH)
        quadraticBezierTo(cx - s * 0.15f, groundY - s * 0.1f, cx - s * 0.1f, groundY)
        moveTo(cx + s * 0.2f, bodyTopY + bodyH)
        quadraticBezierTo(cx + s * 0.15f, groundY - s * 0.1f, cx + s * 0.1f, groundY)
    }
    drawPath(legPath, color = TrouserColor, style = Stroke(width = s * 0.18f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.drawJumpingStudent(cx: Float, groundY: Float, scale: Float) {
    val s = scale
    val headR = s * 0.55f
    val headY = groundY - s * 2.2f

    drawCircle(color = SkinColor, radius = headR, center = Offset(cx, headY))
    drawHair(cx, headY, headR)

    val eyeY = headY - headR * 0.05f
    drawCircle(color = Color(0xFF2C1810), radius = s * 0.06f, center = Offset(cx - headR * 0.3f, eyeY))
    drawCircle(color = Color(0xFF2C1810), radius = s * 0.06f, center = Offset(cx + headR * 0.3f, eyeY))
    drawCircle(color = Color.White, radius = s * 0.025f, center = Offset(cx - headR * 0.25f, eyeY - s * 0.02f))
    drawCircle(color = Color.White, radius = s * 0.025f, center = Offset(cx + headR * 0.35f, eyeY - s * 0.02f))

    drawArc(
        color = Color(0xFFD84315),
        startAngle = 10f,
        sweepAngle = 160f,
        useCenter = false,
        topLeft = Offset(cx - headR * 0.3f, headY + headR * 0.1f),
        size = Size(headR * 0.6f, headR * 0.35f),
        style = Stroke(width = s * 0.06f, cap = StrokeCap.Round)
    )

    drawCheeks(cx, headY, headR)

    val bodyTopY = headY + headR * 0.6f
    val bodyH = s * 1.0f
    val bodyPath = Path().apply {
        moveTo(cx - s * 0.4f, bodyTopY)
        quadraticBezierTo(cx, bodyTopY + s * 0.1f, cx + s * 0.4f, bodyTopY)
        lineTo(cx + s * 0.45f, bodyTopY + bodyH)
        lineTo(cx - s * 0.45f, bodyTopY + bodyH)
        close()
    }
    drawPath(bodyPath, color = Color(0xFF66BB6A))

    drawLine(color = SkinColor, start = Offset(cx - s * 0.43f, bodyTopY + s * 0.35f), end = Offset(cx - s * 1.0f, bodyTopY + s * 0.2f), strokeWidth = s * 0.15f, cap = StrokeCap.Round)
    drawLine(color = SkinColor, start = Offset(cx + s * 0.43f, bodyTopY + s * 0.35f), end = Offset(cx + s * 1.2f, bodyTopY - s * 0.3f), strokeWidth = s * 0.15f, cap = StrokeCap.Round)

    val legPathL = Path().apply {
        moveTo(cx - s * 0.2f, bodyTopY + bodyH)
        quadraticBezierTo(cx - s * 0.6f, groundY - s * 0.3f, cx - s * 0.8f, groundY - s * 0.1f)
    }
    drawPath(legPathL, color = TrouserColor, style = Stroke(width = s * 0.18f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    val legPathR = Path().apply {
        moveTo(cx + s * 0.2f, bodyTopY + bodyH)
        quadraticBezierTo(cx + s * 0.7f, groundY - s * 0.5f, cx + s * 1.0f, groundY - s * 0.2f)
    }
    drawPath(legPathR, color = TrouserColor, style = Stroke(width = s * 0.18f, cap = StrokeCap.Round, join = StrokeJoin.Round))

    drawCircle(color = ShoeColor, radius = s * 0.1f, center = Offset(cx - s * 0.8f, groundY - s * 0.1f))
    drawCircle(color = ShoeColor, radius = s * 0.1f, center = Offset(cx + s * 1.0f, groundY - s * 0.2f))
}

private fun DrawScope.drawHair(cx: Float, headY: Float, headR: Float) {
    val hairPath = Path().apply {
        moveTo(cx - headR * 0.8f, headY - headR * 0.1f)
        quadraticBezierTo(cx - headR * 0.7f, headY - headR * 1.0f, cx, headY - headR * 1.0f)
        quadraticBezierTo(cx + headR * 0.7f, headY - headR * 1.0f, cx + headR * 0.8f, headY - headR * 0.1f)
        quadraticBezierTo(cx + headR * 0.5f, headY - headR * 0.6f, cx, headY - headR * 0.5f)
        quadraticBezierTo(cx - headR * 0.5f, headY - headR * 0.6f, cx - headR * 0.8f, headY - headR * 0.1f)
        close()
    }
    drawPath(hairPath, color = HairColor)
}

private fun DrawScope.drawCheeks(cx: Float, headY: Float, headR: Float) {
    drawCircle(color = Color(0xFFFFCDD2).copy(alpha = 0.6f), radius = headR * 0.15f, center = Offset(cx - headR * 0.5f, headY + headR * 0.2f))
    drawCircle(color = Color(0xFFFFCDD2).copy(alpha = 0.6f), radius = headR * 0.15f, center = Offset(cx + headR * 0.5f, headY + headR * 0.2f))
}
