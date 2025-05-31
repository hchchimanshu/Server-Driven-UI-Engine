# Server-Driven-UI-Engine
This project demonstrates a Server-Driven UI (SDUI) engine built using Jetpack Compose, where the entire UI is defined dynamically from a JSON configuration (**Sample.json**). It provides a scalable and flexible way to render screens based on remote or local layout definitions, with minimal hardcoded logic.

# Key Features
## Dynamic UI Rendering
UI is not hardcoded — it's parsed from a JSON file consisting of three main parts:<br>
**layout:** Defines the screen structure and component hierarchy.<br>
**data:** Contains the actual content for each UI component (like text, image URLs, button labels).<br>
**style:** Defines reusable styles for padding, font size, colors, etc.<br>
## Sealed Class Hierarchy
All UI components are modeled using Kotlin sealed classes (UiComponent) for type-safe rendering of:<br>
-Text<br>
-Image<br>
-Button<br>
-Input<br>
-Spacer<br>
-Row<br>
-Column<br>
-UnknownComponent (for fallback)<br>
## Composable Rendering
The main RenderDynamicUI() function maps parsed components to composables like Text, Image, Button, etc.
## Style Resolution and Modifiers
Style modifiers (getModifier) are dynamically applied using values from JSON:
e.g., fillMaxWidth, padding, marginStart, height, etc.
## User Actions with Navigation
Buttons support JSON-defined action objects like:<br>
-navigate to another screen<br>
-toast to show messages<br>
## Input Validation
Input fields (OutlinedTextField) support validation rules such as required, minLength, and error messages defined in JSON.
## Graceful Error Handling
-Malformed or missing image URLs are handled gracefully.<br>
-Missing styles or unknown component types won't crash the app.<br>
-Fallback UI is shown for incomplete/missing data.<br>

<br>
├── MainActivity.kt       # Entry point that parses and renders dynamic UI<br>
├── UiComponent.kt        # Sealed classes defining all supported UI components<br>
├── SampleJson.kt         # Contains static JSON data (layout, data, style)<br>
└── utils.kt              # Utility functions like getModifier, getTextStyle, validateInput<br>

## How It Works
1. The MainActivity loads JSON strings from SampleJson.kt.
2. It parses the layout, data, and style sections using JSONObject.
3. The engine renders the correct UI tree with proper data and styling.
4. Any action defined (like navigate or toast) is triggered on interaction.

## Technologies Used
*Kotlin<br>
*Jetpack Compose<br>
*Coil (for image loading)<br>
*AndroidX Libraries<br>
*JSONObject for JSON parsing<br>

  
![Media 17_Media 13_Media 16_Media 15_Media 14](https://github.com/user-attachments/assets/e8258d00-9a8b-445b-b32b-8e2f0f7d5723)


