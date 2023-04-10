create table if not exists movies (
    id bigint,
    name varchar not null,
    description text not null,
    genre varchar(50) not null,
    rating float(1) default 0.0,
    release_date date not null,
    primary key(id)
);

create table if not exists actors (
    id bigint,
    name text not null,
    primary key (id)
);

create table if not exists actor_movie (
    movie_id bigint,
    actor_id bigint,
    primary key (movie_id, actor_id),
    constraint movie_fk foreign key(movie_id) references movies(id),
    constraint actor_fk foreign key(actor_id) references actors(id)
);

create sequence if not exists movie_seq start 1 increment 10 owned by movies.id;
create sequence if not exists actor_seq start 1 increment 10 owned by actors.id;
