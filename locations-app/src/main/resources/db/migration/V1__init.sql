create table locations(id int auto_increment primary key, name varchar(255), lat double, lon double, interesting_at datetime);

insert into locations(name, lat, lon) values ('Budapest', 47.497912, 19.040235);
insert into locations(name, lat, lon) values ('Debrecen', 47.5316049, 21.6273124);
insert into locations(name, lat, lon) values ('Miskolc', 48.1034775, 20.7784384);
insert into locations(name, lat, lon) values ('Veszprém', 47.1028087, 17.9093019);
insert into locations(name, lat, lon) values ('Győr', 47.6874569, 17.6503974);

create table location_tags(id int auto_increment primary key, location_id int, tags varchar(255), foreign key (location_id) references locations(id));

