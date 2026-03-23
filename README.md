# 🌐 Translate Machine

A Java Swing desktop translation app powered by **Google Translate free API** — no API key, no signup, no credit card required.

---

## 📋 Requirements

- Java **11 or higher**

Check your version:
```bash
java -version
```

---

## 📁 Project Structure

```
TranslateMachineApp/
├── TranslateMachine.java   # Main window (JFrame) + entry point
├── TranslatePanel.java     # Input/Output text panels
├── ApiClient.java          # Google Translate API call + response parsing
├── UIHelper.java           # Reusable UI component factory
├── AppColors.java          # Color constants (dark theme)
└── README.md
```

---

## 🚀 How to Run

**1. Compile all files:**
```bash
javac *.java
```

**2. Run the app:**
```bash
java TranslateMachine
```

---

## 🌍 Supported Languages

| Language   | Code  |
|------------|-------|
| English    | en    |
| Khmer      | km    |
| Chinese    | zh-CN |
| Japanese   | ja    |
| Korean     | ko    |
| French     | fr    |
| Spanish    | es    |
| German     | de    |
| Vietnamese | vi    |
| Indonesian | id    |
| Arabic     | ar    |
| Portuguese | pt    |
| Russian    | ru    |
| Hindi      | hi    |

---

## ✨ Features

- **15 languages** supported
- **Auto Detect** source language
- **Swap** button — flip source/target language and text
- **Copy** translation to clipboard
- **Clear** button to reset both panels
- **Ctrl+Enter** keyboard shortcut to translate
- Dark themed UI
- Runs translation on background thread (no UI freeze)

---

## 🔌 API

Uses **Google Translate free endpoint**:
```
https://translate.googleapis.com/translate_a/single
```
- ✅ No API key needed
- ✅ No account required
- ✅ No credit card
- ✅ Supports all major languages including Khmer

---

## 📦 Dependencies

**None.** Only Java standard library:

| Library | Purpose |
|---|---|
| `javax.swing.*` | GUI framework |
| `java.awt.*` | UI components & colors |
| `java.net.http.HttpClient` | HTTP requests (Java 11+) |
| `java.net.URLEncoder` | URL encoding |

No Maven, no Gradle, no external JARs needed.

---

