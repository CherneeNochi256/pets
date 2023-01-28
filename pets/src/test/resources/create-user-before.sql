delete
from user_role;
delete
from usr;

insert into usr(id, active, password, username)
values (1, true, '$2a$08$me84TRZ/q70KyFCmQVRi5uycHG8oDTdfybVaUWzxV2ic6uBaXmNSy', 'admin'),
       (2, true, '$2a$08$me84TRZ/q70KyFCmQVRi5uycHG8oDTdfybVaUWzxV2ic6uBaXmNSy', 'maxim');
insert into user_role(user_id, roles)
values (1,'USER'),(1,'ADMIN'),
       (2,'USER');