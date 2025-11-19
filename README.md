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

This project uses a database-driven localization system that allows decks and cards to support multiple languages. Translations are stored in separate tables, keeping the database clean, scalable, and easy to extend.

## Overview

Instead of storing fields like `name_en` or `text_es` in main tables, translations are kept in dedicated translation tables.  
Each entity (deck, card, etc.) stores its original content, and any additional languages are added as separate translation records.

## Why This Design?

- Unlimited language support  
- No schema changes when adding new languages  
- Works for any translatable entity  
- Keeps core tables clean and normalized  
- Easy to manage and extend in OTP2  

## Database Structure

### `languages`
Stores all supported languages.

| Column      | Type        |
|-------------|-------------|
| code        | VARCHAR(5)  |
| name        | VARCHAR(50) |
| native_name | VARCHAR(50) |

## Card Translations — `card_translations`

| Column         | Type        |
|----------------|-------------|
| id             | INT PK AI   |
| card_id        | INT         |
| language_code  | VARCHAR(5)  |
| front_text     | TEXT        |
| back_text      | TEXT        |
| extra_info     | TEXT        |
| updated_at     | TIMESTAMP   |

Each row = translation of one card in one language.


## Deck Translations — `deck_translations`

| Column         | Type        |
|----------------|-------------|
| id             | INT PK AI   |
| deck_id        | INT         |
| language_code  | VARCHAR(5)  |
| deck_name      | TEXT        |
| description    | TEXT        |
| updated_at     | TIMESTAMP   |

Each row = translation of one deck in one language.

---

## OTP2 Features (Planned)

- `?lang=<code>` parameter for API requests  
- Automatic fallback to default language  
- Tools for adding/editing translations  
- Unified translation handling for decks, cards, and future entities  