create table s_source
(
    id   bigint not null primary key,
    name text   not null
);

comment on table s_source is 'Источник данных (маркетплейс)';
comment on column s_source.id is 'Идентификатор';
comment on column s_source.name is 'Наименование';

insert into s_source values (1, 'ВБ');