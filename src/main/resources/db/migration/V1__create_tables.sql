create table if not exists movies (
    id bigint not null,
    title varchar not null,
    synopsis varchar(2000) not null,
    votes bigint default 0,
    rating float(1) default 0.0,
    created_at date not null,
    release_date date not null,
    primary key(id)
);

create table if not exists actors (
    id bigint not null,
    name varchar(255) not null,
    summary varchar(2000) not null,
    birth_date date not null,
    created_at date not null,
    last_modified date not null,
    primary key(id)
);

create table if not exists reviews(
    id bigint not null,
    content varchar(2800) not null,
    rating float(1) not null,
    created_at date not null,
    last_modified date not null,
    movie_id bigint,
    primary key(id),
    constraint movie_fk foreign key (movie_id) references movies(id)
);

create table if not exists actors_movies (
    movie_id bigint not null,
    actor_id bigint not null,
    primary key (movie_id, actor_id),
    constraint movie_fk foreign key(movie_id) references movies(id),
    constraint actor_fk foreign key(actor_id) references actors(id)
);

create sequence if not exists movies_seq start 1 increment 50 owned by movies.id;
create sequence if not exists actors_seq start 1 increment 50 owned by actors.id;
create sequence if not exists reviews_seq start 1 increment 50 owned by reviews.id;
