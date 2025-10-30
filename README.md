# Card Memo

## Overview

Card Memo is a flashcard-based learning app that helps users remember information in a fun and simple way. It’s designed for students, language learners, and anyone preparing for exams.

## Team

- Axel Nokireki
- Vladimir Karpenko
- Georgii Afanasev
- Patrik Skogberg

## Features

- Create and organize custom flashcard decks
- Track learning progress and accuracy
- Share cards and decks with others
- Login and registration support

## Objectives

- Provide learning statistics and progress tracking
- Deliver fast, responsive UI
- Ensure accuracy reports and deck mastery tracking

## Tech Stack

- **Backend:** Java 17+, Maven
- **Frontend/GUI:** JavaFX
- **Database:** MariaDB
- **Testing:** JUnit 5, TestFx
- **DevOps:** Jenkins, Docker, GitHub, Jacoco

## Risk Management

- Training for new tools (Jenkins)
- Agile sprints to manage time constraints
- CI/CD pipelines to reduce integration errors
- Backup and staging environments for deployment

## Testing

- Unit tests with JUnit 5
- Integration tests
- Automated CI/CD with Jenkins

## Documentation & Reporting

- Weekly sprint reports and reviews
- Final project report at completion

## Localization

Currently supported language codes:

- `en` — English  
- `ru` — Russian  
- `ja` — Japanese

Choose language by using lanugage dropdown buttons that can be found across the app.
![Image of language switch button](/public/languageSwitch.png)

# Localization Setup

This project includes a simple internationalization (i18n) system using JavaFX and `ResourceBundle`.  
It allows switching between multiple languages at runtime.

## Overview

The localization system is handled by two main classes:

- **`util.I18n`** — manages the current `Locale` and loads language resource bundles.
- **`LanguageController`** — provides a UI control (`ComboBox`) to select the language and updates the interface dynamically.

## Resource Bundle Location

All translation files are stored in: 
[HERE](src/main/resources/Languages/)


