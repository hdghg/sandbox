create table some_data (
  id serial,
  date_time timestamp with time zone,
  date date,
  first_name varchar(100),
  last_name varchar(100)
);

insert into some_data (date_time, date, first_name, last_name) (
select
    timestamp '2001-01-10 20:00:00' +
       random() * (timestamp '2020-01-10 20:00:00' -
                   timestamp '2001-01-10 10:00:00') as date_time,
    null as date,
    md5(random()::text) as first_name,
    md5(random()::text) as last_name
from generate_series(1, 100000000)
);

update some_data set date = date(date_time);


