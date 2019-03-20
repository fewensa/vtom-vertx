


create table t_article(
id serial not null,
title varchar(255) not null,
content text not null,
ctime timestamp default now(),
primary key(id)
);

create table t_tags(
id serial not null,
aid int not null,
name varchar(100) not null,
ctime timestamp default now(),
primary key(id)
);


insert into t_article (title, content) values ('Hello world', 'Hello world');
insert into t_article (title, content) values ('First article', 'This is my first article');

insert into t_tags(aid, name) values (1, 'hello');
insert into t_tags(aid, name) values (1, 'world');
insert into t_tags(aid, name) values (2, 'first');
insert into t_tags(aid, name) values (2, 'article');

