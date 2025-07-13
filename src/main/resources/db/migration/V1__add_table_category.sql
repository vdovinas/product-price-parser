create table category
(
    id   bigserial not null primary key,
    name text      not null,
    code text      not null
);

comment on table category is 'Категория';
comment on column category.id is 'Идентификатор';
comment on column category.name is 'Наименование';