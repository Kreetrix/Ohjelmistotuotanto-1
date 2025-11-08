-- This file contains a example date for tables with translations of decks and cards.


INSERT INTO decks (deck_id, deck_name, description, user_id, visibility, is_deleted, created_at)
VALUES (101, 'Animals', 'Basic animal vocabulary', 1, 'public', FALSE, NOW()),
       (102, 'Food', 'Common food items', 1, 'public', FALSE, NOW()),
       (103, 'Travel', 'Essential travel phrases', 1, 'public', FALSE, NOW());



INSERT INTO deck_translations (deck_id, language_code, deck_name, description)
VALUES
--  Animals
(101, 'ru', 'Животные', 'Базовые слова о животных'),
(101, 'fi', 'Eläimet', 'Perussanasto eläimistä'),
(101, 'ja', '動物', '基本的な動物の語彙'),

--  Food
(102, 'ru', 'Еда', 'Основные продукты питания'),
(102, 'fi', 'Ruoka', 'Yleiset ruoka-aineet'),
(102, 'ja', '食べ物', '一般的な食べ物'),

--  Travel
(103, 'ru', 'Путешествия', 'Основные фразы для путешествий'),
(103, 'fi', 'Matkustaminen', 'Tärkeitä lauseita matkustamiseen'),
(103, 'ja', '旅行', '旅行の基本フレーズ');



INSERT INTO cards (card_id, deck_id, front_text, back_text, image_url, extra_info, is_deleted)
VALUES
--  Animals
(201, 101, 'Cat', 'A small domestic animal', NULL, NULL, FALSE),
(202, 101, 'Dog', 'A loyal animal often kept as a pet', NULL, NULL, FALSE),
(203, 101, 'Elephant', 'A large animal with a trunk', NULL, NULL, FALSE),

--  Food
(301, 102, 'Apple', 'A sweet red or green fruit', NULL, NULL, FALSE),
(302, 102, 'Bread', 'A baked food made from flour', NULL, NULL, FALSE),
(303, 102, 'Cheese', 'A dairy product made from milk', NULL, NULL, FALSE),

--  Travel
(401, 103, 'Ticket', 'A document that allows travel', NULL, NULL, FALSE),
(402, 103, 'Airport', 'A place where planes take off', NULL, NULL, FALSE),
(403, 103, 'Hotel', 'A place to stay while traveling', NULL, NULL, FALSE);



INSERT INTO card_translations (card_id, language_code, front_text, back_text)
VALUES
--
(201, 'ru', 'Кошка', 'Маленькое домашнее животное'),
(202, 'ru', 'Собака', 'Преданный питомец человека'),
(203, 'ru', 'Слон', 'Крупное животное с хоботом'),

(201, 'fi', 'Kissa', 'Pieni kotieläin'),
(202, 'fi', 'Koira', 'Uskollinen lemmikki'),
(203, 'fi', 'Norsu', 'Iso eläin, jolla on kärsä'),

(201, 'ja', '猫', '小さな飼いならされた動物'),
(202, 'ja', '犬', '忠実なペット'),
(203, 'ja', '象', '鼻のある大きな動物'),

--
(301, 'ru', 'Яблоко', 'Сладкий красный или зелёный фрукт'),
(302, 'ru', 'Хлеб', 'Выпечка из муки'),
(303, 'ru', 'Сыр', 'Молочный продукт из молока'),

(301, 'fi', 'Omena', 'Makea punainen tai vihreä hedelmä'),
(302, 'fi', 'Leipä', 'Jauhoista leivottu ruoka'),
(303, 'fi', 'Juusto', 'Maitotuote, joka on valmistettu maidosta'),

(301, 'ja', 'りんご', '甘い赤または緑の果物'),
(302, 'ja', 'パン', '小麦粉から焼いた食べ物'),
(303, 'ja', 'チーズ', 'ミルクから作られた乳製品'),

--
(401, 'ru', 'Билет', 'Документ, позволяющий путешествовать'),
(402, 'ru', 'Аэропорт', 'Место, где взлетают самолёты'),
(403, 'ru', 'Отель', 'Место для проживания во время путешествий'),

(401, 'fi', 'Lippu', 'Matkustusasiakirja'),
(402, 'fi', 'Lentokenttä', 'Paikka, josta lentokoneet lähtevät'),
(403, 'fi', 'Hotelli', 'Majoituspaikka matkustaessa'),

(401, 'ja', 'チケット', '旅行を許可する書類'),
(402, 'ja', '空港', '飛行機が離陸する場所'),
(403, 'ja', 'ホテル', '旅行中の宿泊場所');
