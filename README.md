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

# Localization System

This project includes a database-based localization system that enables multi-language support for various entities (such as cards and decks). The system is prepared but will be fully integrated in OTP2.

## Overview

Instead of storing language-specific columns (such as name_en, name_es, etc.) inside each entity table, this system uses a dedicated localization table. This keeps the database normalized and makes it easy to add new languages without modifying the schema.

## Why This Method

- Scales to any number of languages  
- Works for all entity types  
- Requires no schema changes when adding languages  
- Keeps the database clean and maintainable  
- Allows easy expansion in OTP2

## Database Schema

Table: `localization`

| Column          | Type        | Description                      |
|-----------------|-------------|----------------------------------|
| translation_id  | INT PK AI   | Unique translation entry         |
| entity_type     | VARCHAR     | Type of entity (e.g., card)      |
| entity_id       | INT         | ID of the entity                 |
| language_code   | VARCHAR(5)  | ISO code (e.g., en, es, fr)      |
| translated_text | TEXT        | The translated content           |

Planned Usage (OTP2)

Backend will fetch localized text when the ?lang=<code> parameter is used

Fallback to default language when translation is missing

CRUD operations for translation management will be introduced

API responses will automatically include translated content when available


