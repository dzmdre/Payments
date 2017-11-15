drop database payment_db;
create database if not exists payment_db;
use payment_db;

create table if not exists payment_user (
    user_id bigint AUTO_INCREMENT not null,
    username varchar(255),
    password varchar(255),
    user_role ENUM('USER','ADMIN'),
    primary key(user_id)
) ENGINE=InnoDB;


create table if not exists card (
    card_id bigint AUTO_INCREMENT not null,
    card_type varchar(255),
    card_number varchar(255),
    card_date date,
    user_id bigint not null,
    account_id bigint not null,
    primary key (card_id)
) ENGINE=InnoDB;

create table if not exists account (
    account_id bigint AUTO_INCREMENT not null,
    locked bool,
    account_number varchar(255),
    sum bigint,
    user_id bigint not null,
    card_id bigint not null,
    primary key (account_id)
) ENGINE=InnoDB;

create table if not exists payment (
    payment_id bigint AUTO_INCREMENT not null,
    payment_date date,
    sum bigint,
    account_id bigint not null,
    index(account_id),
    primary key (payment_id)
) ENGINE=InnoDB;

alter table payment
    ADD foreign key (account_id) references account(account_id);

alter table account
    ADD foreign key (card_id) references card(card_id);
alter table account
    ADD foreign key (user_id) references payment_user(user_id);

alter table card
    ADD foreign key (user_id) references payment_user(user_id);
alter table card
    ADD foreign key (account_id) references account(account_id);