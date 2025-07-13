create table product
(
    id                bigserial                not null primary key,
    name              text                     not null,
    external_id       text                     not null,
    price             text                     not null,
    source_id         bigint                   not null,
    subcategory_id    bigint                   not null,
    last_updated_date timestamp with time zone not null,
    constraint fk_product_subcategory_id foreign key (subcategory_id)
        references subcategory (id),
    constraint fk_product_source_id foreign key (source_id)
        references s_source (id)
);

comment on table product is 'Товары';
comment on column product.id is 'Идентификатор';
comment on column product.name is 'Наименование товара';
comment on column product.external_id is 'Идентификатор товара в источнике';
comment on column product.price is 'Цена';
comment on column product.subcategory_id is 'Подкатегория';
comment on column product.source_id is 'Источник';
comment on column product.last_updated_date is 'Дата/время последнего обновления';

create index idx_product_subcategory_id on product (subcategory_id);
create index idx_product_source_id_external_id on product (source_id, external_id);