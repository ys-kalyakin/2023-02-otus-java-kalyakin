create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create sequence address_seq start with 1 increment by 1;
create sequence phone_seq start with 1 increment by 1;

create table address (id bigserial not null, street varchar(255), client_id bigint, primary key (id));
create table phone (id bigserial not null, number varchar(255), client_id bigint, primary key (id));
alter table if exists address add constraint FK7156ty2o5atyuy9f6kuup9dna foreign key (client_id) references client;
alter table if exists phone add constraint FK3o48ec26lujl3kf01hwqplhn2 foreign key (client_id) references client;
