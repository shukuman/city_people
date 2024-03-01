drop table if exists cities;
create table cities
(
    id serial8,
    city_name varchar(30) not null,
    primary key (id),
    unique (city_name)
);

create table people
(
    id serial8,
    city_id int4 not null,
    person_name varchar(30) not null,
    primary key (id),
    foreign key (city_id) references cities (id),
    unique (person_name)
);

insert into cities (city_name)
values ('Astana'),
       ('Almaty'),
       ('London');

insert into people (city_id, person_name)
values (1, 'Dariya'),
       (2, 'Amira'),
       (3, 'Saltanat');

select p.person_name
from people p
where p.city_id = 3