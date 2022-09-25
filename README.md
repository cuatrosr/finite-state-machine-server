# ***Finite State Machine | Back End*** 💻️

<p style="text-align: justify">
    <i>Web application that allows it to get a related or minimum mealy machine and decide if two machines are equivalent</i>
</p>

## ***Build With*** 🛠️

<div style="text-align: left">
    <p>
        <a href="https://www.jetbrains.com/es-es/idea/" target="_blank"> <img alt="IntelliJ Idea" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/intellij/intellij-original.svg" height="60" width = "60"></a>
        <a href="https://www.java.com/es/" target="_blank"> <img alt="Java" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/java/java-original.svg" height="60" width = "60"></a>
        <a href="https://spring.io" target="_blank"> <img alt="Spring" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/spring/spring-original.svg" height="60" width = "60"></a>
    </p>
</div>

## ***Versioned*** 📌

<div style="text-align: left">
    <a href="https://git-scm.com/" target="_blank"> <img src="https://raw.githubusercontent.com/devicons/devicon/2ae2a900d2f041da66e950e4d48052658d850630/icons/git/git-original.svg" height="60" width = "60" alt="Git"></a>
    <a href="https://github.com/" target="_blank"> <img src="https://img.icons8.com/fluency-systems-filled/344/ffffff/github.png" height="60" width = "60" alt="GitHub"></a>
</div>

## ***Local Deploy*** 📦

*To run the web app you should follow these steps:*

1️⃣ *Clone or download the repository*

2️⃣ *Clean and install the maven dependencies*

3️⃣ *Run the application*

## ***Json Content Type*** 📧

*The Json content for each Http Request's must follow this format:*

*For Mealy Machine:*
```json
{
    "initialState": "[I0]",
    "stimulus": ["[S1]", "[S2]", "[...]"],
    "states": [
      {
        "root":"[I0]",
        "states": [
          {
            "state": "[In]",
            "response": "[Sn]"
          },
          {
            "state": "[...]",
            "response": "[...]"
          }
        ]
      },
      {
        "root":"[...]",
        "states": [
          {
            "state": "[...]",
            "response": "[...]"
          },
          {
            "state": "[...]",
            "response": "[...]"
          }
        ]
      }
    ]
}
```

*For Moore Machine:*
```json
{
  "initialState": "[I0]",
  "stimulus": ["[S1]", "[S2]" , "[...]"],
  "states": [
    {
      "root": "[I0]",
      "response": "[Sn]",
      "states": ["[Ix]", "[Iy]", "[...]"]
    },
    {
      "root": "[...]",
      "response": "[...]",
      "states": ["[...]", "[...]", "[...]"]
    }
  ]
}
```

*For Any Equivalent Machine validation just need an array of the two machines*

*There's a postman collection for test purposes [here]()*

## ***Author*** ✒️

<div style="text-align: left">
    <a href="https://github.com/cuatrosr" target="_blank"> <img alt="cuatrosr" src="https://images.weserv.nl/?url=avatars.githubusercontent.com/u/70908378?v=4&h=60&w=60&fit=cover&mask=circle"></a>
</div>

---
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/for-you.svg)](https://forthebadge.com)