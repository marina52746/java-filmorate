delete from user_friend;
delete from user_like;
delete from "USER";
delete from film_genre;
delete from genre;
delete from film;
delete from mpa_rating;

--drop table user_friend cascade;
--drop table user_like cascade;
--drop table "USER" cascade;
--drop table film_genre cascade;
--drop table genre cascade;
--drop table film cascade;
--drop table mpa_rating cascade;

MERGE INTO PUBLIC."MPA_RATING" (mpa_rating_id, name) KEY (MPA_RATING_ID) values ( 1, 'G');
MERGE INTO PUBLIC."MPA_RATING" (mpa_rating_id, name) KEY (MPA_RATING_ID) values ( 2, 'PG');
MERGE INTO PUBLIC."MPA_RATING" (mpa_rating_id, name) KEY (MPA_RATING_ID) values ( 3, 'PG-13');
MERGE INTO PUBLIC."MPA_RATING" (mpa_rating_id, name) KEY (MPA_RATING_ID) values ( 4, 'R');
MERGE INTO PUBLIC."MPA_RATING" (mpa_rating_id, name) KEY (MPA_RATING_ID) values ( 5, 'NC-17');

MERGE INTO PUBLIC."GENRE" (genre_id, name) KEY(genre_id)  values ( 1, 'Комедия');
MERGE INTO PUBLIC."GENRE" (genre_id, name) KEY(genre_id)  values ( 2, 'Драма');
MERGE INTO PUBLIC."GENRE" (genre_id, name) KEY(genre_id)  values ( 3, 'Мультфильм');
MERGE INTO PUBLIC."GENRE" (genre_id, name) KEY(genre_id)  values ( 4, 'Триллер');
MERGE INTO PUBLIC."GENRE" (genre_id, name) KEY(genre_id)  values ( 5, 'Документальный');
MERGE INTO PUBLIC."GENRE" (genre_id, name) KEY(genre_id)  values ( 6, 'Боевик');
