package com.himanshuhc.jsontoui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.himanshuhc.jsontoui.ui.UiComponent
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentScreen by remember { mutableStateOf("home") }
            val context = LocalContext.current
            val layoutJson = JSONObject(SampleJson.layout)
            val dataJson = JSONObject(SampleJson.data)
            val styleJson = JSONObject(SampleJson.style)

            val currentLayout = layoutJson
                .getJSONObject("screens")
                .optJSONObject(currentScreen) ?: JSONObject()

            RenderDynamicUI(
                layout = currentLayout,
                data = dataJson,
                styles = styleJson,
                onAction = { action ->
                    handleAction(action, { newScreen -> currentScreen = newScreen }, context)
                }
            )
        }
    }
}

    private fun handleAction(action: JSONObject, setCurrentScreen: (String) -> Unit, context: Context) {

        val type = action.optString("type")
        when (type) {
            "navigate" -> {
                val screen = action.optString("destination")
                // TODO: Trigger navigation logic here
                Toast.makeText(context, "Navigate to $screen", Toast.LENGTH_SHORT).show()
                setCurrentScreen(screen)
            }

            "toast" -> {
                val message = action.optString("message", "Default Message")
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(context, "Unknown action: $type", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun RenderDynamicUI(
        layout: JSONObject,
        data: JSONObject,
        styles: JSONObject,
        onAction: (JSONObject) -> Unit
    ) {

        val parsedComponent = parseComponent(layout)
        RenderComponent(parsedComponent, data, styles, onAction)
    }

    @Composable
    fun RenderComponent(
        component: UiComponent,
        data: JSONObject,
        style: JSONObject,
        onAction: (JSONObject) -> Unit
    ) {

        when (component) {
            is UiComponent.TextComponent -> {
                val textData = data.optJSONObject(component.id)
                val text = textData?.optString("text") ?: ""
                val styleName = textData?.optString("style")
                val styleJson = styleName?.let { style.optJSONObject(it) }

                Text(
                    text = text,
                    style = getTextStyle(styleJson),
                    modifier = getModifier(styleJson)
                )
            }

            is UiComponent.ImageComponent -> {
                val imageData = data.optJSONObject(component.id)
                val url = imageData?.optString("url")
                val styleJson = imageData?.optJSONObject("style")

                if (!url.isNullOrBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = url),
                        contentDescription = null,
                        modifier = getModifier(styleJson),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = getModifier(styleJson).background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Image unavailable", color = Color.White)
                    }
                }
            }


            is UiComponent.ButtonComponent -> {
                val btnData = data.optJSONObject(component.id)
                val text = btnData?.optString("text") ?: ""
                val action = btnData?.optJSONObject("action")
                val resolvedStyle = resolveStyle(btnData, style)

                Button(
                    modifier = getModifier(resolvedStyle),
                    colors = ButtonDefaults.buttonColors(containerColor = getColor(resolvedStyle)),
                    onClick = {
                        // Just trigger action regardless of validation
                        action?.let(onAction)
                    }
                ) {
                    Text(text = text, style = getTextStyle(resolvedStyle))
                }
            }


            is UiComponent.SpacerComponent -> {
                val id = component.id
                val size = data.optJSONObject(id)?.optInt("size") ?: 0
                val spacerModifier = if (id.contains("spacerRow", ignoreCase = true)) {
                    Modifier.width(size.dp)
                } else {
                    Modifier.height(size.dp)
                }
                Spacer(modifier = spacerModifier)
            }

            is UiComponent.InputComponent -> {
                val inputData = data.optJSONObject(component.id)
                val placeholder = inputData?.optString("placeholder") ?: ""

                val validation = inputData?.optJSONObject("validation")

                val resolvedStyle = resolveStyle(inputData, style)

                var value by remember { mutableStateOf("") }
                var errorText by remember { mutableStateOf<String?>(null) }

                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        errorText = validateInput(it, validation)
                    },
                    placeholder = { Text(placeholder) },
                    isError = errorText != null,
                    modifier = getModifier(resolvedStyle)
                )

                if (errorText != null) {
                    Text(
                        text = errorText!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }


            is UiComponent.RowComponent -> {
                Row(
                    modifier = getModifier(style.optJSONObject(component.style ?: "")),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    component.children.forEach { RenderComponent(it, data, style, onAction) }
                }
            }

            is UiComponent.ColumnComponent -> {
                Column(
                    modifier = getModifier(style.optJSONObject(component.style ?: "")),
                    verticalArrangement = Arrangement.Top
                ) {
                    component.children.forEach { RenderComponent(it, data, style, onAction) }
                }
            }

            is UiComponent.UnknownComponent -> {
                Text("Unknown component type: ${component.type}")
            }
        }
    }

    fun parseComponent(json: JSONObject): UiComponent {
        val type = json.optString("type")
        val id = json.optString("id", "")
        val style = json.optString("style", null)

        return when (type) {
            "text" -> UiComponent.TextComponent(id, style)
            "image" -> UiComponent.ImageComponent(id)
            "button" -> UiComponent.ButtonComponent(id)
            "spacer" -> UiComponent.SpacerComponent(id)
            "input" -> UiComponent.InputComponent(id)
            "row" -> {
                val children = json.optJSONArray("children")?.let { parseChildren(it) } ?: emptyList()
                UiComponent.RowComponent(children, style)
            }

            "column" -> {
                val children = json.optJSONArray("children")?.let { parseChildren(it) } ?: emptyList()
                UiComponent.ColumnComponent(children, style)
            }

            else -> UiComponent.UnknownComponent(id, type)
        }
    }

    fun parseChildren(jsonArray: JSONArray): List<UiComponent> {
        val list = mutableListOf<UiComponent>()
        for (i in 0 until jsonArray.length()) {
            list.add(parseComponent(jsonArray.getJSONObject(i)))
        }
        return list
    }

    fun resolveStyle(componentData: JSONObject?, styleMap: JSONObject): JSONObject? {
        val styleId = componentData?.optString("style") ?: return null
        return styleMap.optJSONObject(styleId)
    }

    fun getColor(style: JSONObject?): Color {
        val colorString = style?.optString("backgroundColor", "")?.trim()
        return try {
            if (!colorString.isNullOrEmpty()) Color(android.graphics.Color.parseColor(colorString))
            else Color.Gray
        } catch (e: Exception) {
            Color.Gray
        }
    }

    fun getTextStyle(style: JSONObject?): TextStyle {
        var textStyle = TextStyle.Default
        style?.let {
            if (it.has("fontSize")) {
                textStyle = textStyle.copy(fontSize = it.getInt("fontSize").sp)
            }
            if (it.has("textColor")) {
                val colorStr = it.getString("textColor")
                try {
                    val parsedColor = AndroidColor.parseColor(colorStr)
                    textStyle = textStyle.copy(color = Color(parsedColor))
                } catch (e: IllegalArgumentException) {
                    textStyle = textStyle.copy(color = Color.Black)
                }
            }
        }
        return textStyle
    }

    fun getModifier(style: JSONObject?): Modifier {
        var modifier: Modifier = Modifier

        style?.let {
            if (it.has("padding")) {
                modifier = modifier.then(Modifier.padding(it.getInt("padding").dp))
            }
            if (it.has("paddingStart")) {
                modifier = modifier.then(Modifier.padding(start = it.getInt("paddingStart").dp))
            }
            if (it.optBoolean("fillMaxWidth", false)) {
                modifier = modifier.then(Modifier.fillMaxWidth())
            }
            if (it.has("width")) {
                modifier = modifier.then(Modifier.width(it.getInt("width").dp))
            }
            if (it.has("height")) {
                modifier = modifier.then(Modifier.height(it.getInt("height").dp))
            }

            if (it.optBoolean("fillMaxSize", false)) {
                modifier = modifier.then(Modifier.fillMaxSize())
            }
            if (style.has("marginStart")) {
                val marginStart = style.optInt("marginStart")
                modifier = modifier.then(Modifier.padding(start = marginStart.dp))
            }
        }

        return modifier
    }

    fun validateInput(input: String, validation: JSONObject?): String? {
        validation ?: return null
        if (validation.optBoolean("required", false) && input.isBlank()) {
            return validation.optString("errorMessage", "Field is required.")
        }
        if (validation.has("minLength") && input.length < validation.getInt("minLength")) {
            return validation.optString("errorMessage", "Input is too short.")
        }
        return null
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewDynamicUiRenderer() {
        var currentScreen by remember { mutableStateOf("home") }
        val context = LocalContext.current
        val layoutJson = JSONObject(SampleJson.layout)
        val dataJson = JSONObject(SampleJson.data)
        val styleJson = JSONObject(SampleJson.style)

        val currentLayout = layoutJson
            .getJSONObject("screens")
            .optJSONObject(currentScreen) ?: JSONObject()
        RenderDynamicUI(
            layout = currentLayout,
            data = dataJson,
            styles = styleJson,
            onAction = { action ->
                handleAction(action, { newScreen -> currentScreen = newScreen }, context)
            }
        )
}