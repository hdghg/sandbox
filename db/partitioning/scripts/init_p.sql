create table part_data (
  id serial,
  date_time timestamp with time zone,
  date date,
  first_name varchar(100),
  last_name varchar(100)
) PARTITION BY RANGE (date_time);

CREATE TABLE part_data_y00 PARTITION OF part_data
    FOR VALUES FROM ('2001-01-10') TO ('2010-01-10');

CREATE TABLE part_data_y01 PARTITION OF part_data
    FOR VALUES FROM ('2010-01-10') TO ('2020-01-10');

CREATE TABLE part_data_y02 PARTITION OF part_data
    FOR VALUES FROM ('2020-01-10') TO ('2030-01-10');


insert into part_data (date_time, date, first_name, last_name) (
select
    timestamp '2001-01-10 20:00:00' +
       random() * (timestamp '2020-01-10 20:00:00' -
                   timestamp '2001-01-10 10:00:00') as date_time,
    null as date,
    md5(random()::text) as first_name,
    md5(random()::text) as last_name
from generate_series(1, 100000000)
);




