create table pet_likes(
    user_id bigint not null references usr,
    pet_id bigint not null references pet,
    primary key (user_id, pet_id)
)