create table category_meta_data
(
    id          bigserial not null primary key,
    code        text      not null,
    value       text      not null,
    description text      not null,
    subcategory_id bigint    not null,
    source_id   bigint    not null,
    constraint fk_category_meta_data_subcategory_id foreign key (subcategory_id)
        references subcategory (id),
    constraint fk_category_meta_data_source_id foreign key (source_id)
        references s_source (id)
);

comment on table category_meta_data is 'Параметры категории для формирования запросов к маркетплейсам';
comment on column category_meta_data.id is 'Идентификатор';
comment on column category_meta_data.code is 'Код параметра';
comment on column category_meta_data.value is 'Значения параметра';
comment on column category_meta_data.description is 'Описание параметра';
comment on column category_meta_data.subcategory_id is 'Подкатегория, к которой применяются параметры';
comment on column category_meta_data.source_id is 'Источник, к которому применяются параметры';

create index idx_category_meta_data_subcategory_id on category_meta_data (subcategory_id);