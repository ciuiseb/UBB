use [PRPRACTIC1]
-- 1
create table Participanti(
	id int primary key identity(1,1), 
	name varchar(64) not null, 
	phone varchar(10) not null, 
	address varchar(64) not null
)

create table Dovleci(
	id int primary key identity(1,1), 
	finished bit not null, 
	finished_at datetime, 
	total_time_in_hours int,
	CONSTRAINT CHK_Finished_Date 
    CHECK ((finished = 1 AND finished_at IS NOT NULL) 
        OR (finished = 0 AND finished_at IS NULL))

)

create table ParticipantDovleac(
	p_id int foreign key references Participanti(id), 
	d_id int foreign key references Dovleci(id), 
	descriere_contributie varchar(100),
	primary key(p_id, d_id)
)

create table Evaluator(
	id int primary key identity(1,1), 
	nume varchar(50) not null, 
	birthdate date
)

create table Note(
	e_id int foreign key references Evaluator(id), 
	d_id int foreign key references Dovleci(id), 
	nota int check(nota between 1 and 10 ), 
	primary key(e_id, d_id)
)

--2
go
create procedure SaveDovleacDescription
	@p_id int, 
	@d_id int, 
	@descriere_contributie varchar(100)
as
begin
	if exists
		(
		select * from ParticipantDovleac pd
		where pd.p_id = @p_id and pd.d_id = @d_id
		)
		begin
			update ParticipantDovleac
			set descriere_contributie = @descriere_contributie
			where p_id = @p_id and d_id = @d_id
		end

	else
		begin
			insert into ParticipantDovleac(p_id, d_id, descriere_contributie)
			values (@p_id, @d_id, @descriere_contributie)
		end
end

insert into Participanti(name, phone, address) values
('p1', '1', 'a1'), 
('p2', '2', 'a2'),
('p3', '2', 'a2'),
('p4', '2', 'a2'),
('p5', '2', 'a2')

exec SaveDovleacDescription @p_id = 1, @d_id = 1, @descriere_contributie = 'a muncit mult si prost'
exec SaveDovleacDescription @p_id = 1, @d_id = 1, @descriere_contributie = 'a muncit mult si bine'

select * from ParticipantDovleac

--3
go
create view TopParticipant as
select top 1 with ties 
	p.name, 
	count(*) as nr_dovleci
from Participanti p 
join ParticipantDovleac pd on p.id = pd.p_id
join Dovleci d on d.id = pd.d_id
where d.finished = 1
group by p.id, p.name
order by count(*) desc

select * from TopParticipant

exec SaveDovleacDescription @p_id = 2, @d_id = 3, @descriere_contributie = ''