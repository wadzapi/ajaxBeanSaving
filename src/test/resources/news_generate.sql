-- Удаление ранее созданных для новостей таблиц
DROP TABLE IF EXISTS news_localized;
DROP TABLE IF EXISTS news;


-- Создание необходимых для новостей таблиц
CREATE TABLE news (id Serial PRIMARY KEY, appearance_id NUMERIC(1) NOT NULL, date TIMESTAMP NOT NULL, important NUMERIC(1) NOT NULL, author VARCHAR(64));
CREATE TABLE news_localized (id Serial PRIMARY KEY, news_id INTEGER references news(id), localization_type VARCHAR(255) NOT NULL, subject VARCHAR(255), content TEXT NOT NULL);

-- Добаление новостей c Локализацией
INSERT INTO news VALUES (DEFAULT, 0, CURRENT_TIMESTAMP, 0, 'Steve');
INSERT INTO news_localized VALUES (DEFAULT, (SELECT CURRVAL('news_id_seq')), 'ru_RU', 'Тема обычной новости Стива', 'Обычная новость Стива');
INSERT INTO news_localized VALUES (DEFAULT, (SELECT CURRVAL('news_id_seq')), 'en_US', 'Steve news subject', 'Steve news');

INSERT INTO news VALUES (DEFAULT, 0, CURRENT_TIMESTAMP, 1, 'Steve');
INSERT INTO news_localized VALUES (DEFAULT, (SELECT CURRVAL('news_id_seq')), 'ru_RU', 'Тема важной новости Стива', 'Важная новость Стива');
INSERT INTO news_localized VALUES (DEFAULT, (SELECT CURRVAL('news_id_seq')), 'en_US', 'Steve important news subject', 'Steve important news');