# Sandbox for partitioning

## running

    docker compose up -d

then run

    docker compose exec db psql -U postgres

execute init.sql

    \i /scripts/init.sql

optionally add idx

    create index idx_dt on some_data(date);

shutdown

    docker compose down -v