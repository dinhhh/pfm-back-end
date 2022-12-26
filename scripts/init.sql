create database if not exists pfm_dev;

create table `pfm-dev`.category
(
    category_no varchar(255) not null
        primary key,
    description varchar(255) null
);

create table `pfm-dev`.user
(
    user_no   varchar(255) not null
        primary key,
    address   varchar(255) null,
    email     varchar(255) not null,
    full_name varchar(255) null,
    password  varchar(255) not null,
    phone     varchar(255) null,
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email)
);

create table `pfm-dev`.expense_income
(
    expense_income_no varchar(255) not null
        primary key,
    amount            bigint       not null,
    create_on         datetime(6)  null,
    description       varchar(255) null,
    operation_type    varchar(255) null,
    user_no           varchar(255) not null,
    constraint FKisf2okombk9xcnxee9grvxxpo
        foreign key (user_no) references `pfm-dev`.user (user_no)
);

create table `pfm-dev`.sub_category
(
    category_no    varchar(255) not null
        primary key,
    create_on      datetime(6)  null,
    description    varchar(255) null,
    name           varchar(255) not null,
    operation_type varchar(255) not null,
    parent_id      varchar(255) null,
    user_no        varchar(255) not null,
    constraint FKkeytvve4kkl0b30ntf279xggw
        foreign key (user_no) references `pfm-dev`.user (user_no),
    constraint FKlqrv1aj0pon999jbi5esfpe4k
        foreign key (parent_id) references `pfm-dev`.category (category_no)
);