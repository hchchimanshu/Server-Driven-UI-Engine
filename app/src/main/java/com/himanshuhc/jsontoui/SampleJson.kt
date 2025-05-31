package com.himanshuhc.jsontoui

object SampleJson {
    val layout = """
        {
         "screens": {
                "home": {
                    "type": "column",
                    "style": "columnStyle",
                          "children": [
                            {
                              "type": "text",
                              "id": "headerText",
                              "style": "headerTextStyle"
                            },
                            {
                              "type": "spacer",
                              "id": "spacerHeader"
                            },
                            {
                              "type": "image",
                              "id": "headerImage"
                            },
                            {
                              "type": "spacer",
                              "id": "spacerHeader"
                            },
                            {
                              "type": "row",
                              "style": "rowStyle",
                                    "children": [
                                      {
                                        "type": "button",
                                        "id": "learnMoreButton"
                                      },
                                      {
                                        "type": "spacer",
                                        "id": "spacerRow"
                                      },
                                      {
                                        "type": "button",
                                        "id": "getStartedButton"
                                      }
                                    ]
                            },
                            {
                              "type": "spacer",
                              "id": "spacerFooter"
                            },
                            {
                              "type": "text",
                              "id": "footerText"
                            }
                          ]
                    },
              "detailScreen": {
                    "type": "column",
                     "style": "columnStyle", 
                              "children": [
                                {
                                    "type": "spacer",
                                    "id": "spacerFooter"
                                },
                                { 
                                    "type": "text", 
                                    "id": "detailHeader" 
                                },
                                {
                                    "type": "spacer",
                                    "id": "spacerBetweenInput"
                                },
                                { 
                                    "type": "input",
                                    "id": "nameInput" 
                                },
                                {
                                    "type": "spacer",
                                    "id": "spacerBetweenInput"
                                },
                                {
                                  "type": "button",
                                  "id": "submitButton"
                                },
                                {
                                  "type": "button",
                                  "id": "goBackButton"
                                }
                              ]
                    }
              }
        }
    """.trimIndent()

    val data = """
        {
          "headerText": {
            "text": "🚀 Welcome to Dynamic UI!",
            "style": "headerTextStyle"
          },
          "headerImage": {
            "url": "https://picsum.photos/200",
            "style": {
                "height": 200
                }
          },
          "learnMoreButton": {
            "text": "Learn More",
            "action": {
              "type": "toast",
              "message": "Learn more clicked!"
            }
          },
          "getStartedButton": {
            "text": "Get Started",
            "action": {
              "type": "navigate",
              "destination": "detailScreen"
            }
          },
          "footerText": {
            "text": "✨ Powered by Compose + JSON ✨"
          },
          "spacerHeader": {
            "size": 24
          },
          "spacerBetweenInput": {
            "size": 18
          },
          "spacerRow": {
            "size": 16
          },
          "spacerFooter": {
            "size": 32
          },
            "nameInput": {
              "text": "",
              "placeholder": "Enter your name",
              "style": "detailScreenInputStyle",
              "validation": {
                "required": true,
                "minLength": 3,
                "maxLength": 30,
                "errorMessage": "Name must be at least 3 characters long."
              }
            },
            "detailHeader": {
                "text": "Welcome to the detail screen",
                "style": "detailScreenStyle"
            },
            "submitButton": {
                "text": "Submit",
                "style": "detailScreenButtonStyle",
                "action": {
                  "type": "toast",
                  "message": "Successfully Sumbitted!"
                }
            },
            "goBackButton": {
              "text": "Go Back",
              "style": "detailScreenButtonStyle",
              "action": {
                "type": "navigate",
                "destination": "home"
              }
            }
        }
    """.trimIndent()

    val style = """
        {
             "headerTextStyle": {
              "fontSize": 22,
              "textColor": "#000000",
              "textAlign": "center",
              "padding": 8
            },
            "titleStyle": {
              "fontSize": 24,
              "paddingStart": 16,
              "textColor": "#FF0000"
            },
            "rowStyle": {
              "fillMaxWidth": true,
              "verticalAlignment": "center"
            },
            "columnStyle": {
              "fillMaxSize": true,
              "padding": 16,
              "verticalArrangement": "top"
            },
            "detailScreenInputStyle": {
              "width": 300, 
              "height": 50,
              "marginStart": 16,  
              "fontSize": 18,
              "padding": 2
            },
              "detailScreenStyle": {
                "fillMaxWidth": true,
                "marginStart": 12,
                "fontSize": 18,
                "padding": 5,
                "height": 30
            },
            "detailScreenButtonStyle": {
              "fillMaxWidth": true,
              "padding": 8,
              "verticalAlignment": "center"
            }
        }
    """.trimIndent()
}