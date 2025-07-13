create table subcategory
(
    id                   bigserial not null primary key,
    name                 text      not null,
    is_active            boolean   not null,
    min_discount_percent int       not null,
    page_count           int       not null,
    category_id          bigint    not null,
    constraint fk_subcategory_category_id foreign key (category_id)
        references subcategory (id)
);

comment on table subcategory is 'Подкатегория';
comment on column subcategory.id is 'Идентификатор';
comment on column subcategory.name is 'Наименование';
comment on column subcategory.is_active is 'Признак активности';
comment on column subcategory.min_discount_percent is 'Порог скидки для отправки уведомления';
comment on column subcategory.page_count is 'Количество загружаемых страниц';
comment on column subcategory.category_id is 'Категория';

create index idx_subcategory_theme_id on subcategory (category_id) where is_active = true;