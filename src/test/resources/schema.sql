drop table if exists item CASCADE ;
drop table if exists to_do_list CASCADE ;
create table item (id bigint generated by default as identity, done boolean not null, name varchar(255), td_list_id bigint, primary key (id));
create table to_do_list (id bigint generated by default as identity, name varchar(255) not null, primary key (id));
alter table item add constraint FKap6a52yti1kth9ivnx2euvk90 foreign key (td_list_id) references to_do_list on delete cascade;