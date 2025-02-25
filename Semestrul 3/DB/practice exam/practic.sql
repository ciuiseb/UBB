use [PRACTIC]

--1
create table Trupe(
	id int primary key identity(1,1), 
	nume varchar(50) not null, 
	descriere varchar(50), 
	infiintare date
)

create table Colindatori(
	id int primary key identity(1,1), 
	nume varchar(25), 
	prenume varchar(25), 
	mail varchar(50) unique, 
	data_nastere date, 
	t_id int foreign key references Trupe(id)
)

create table Orase(
	id int primary key identity(1,1), 
	numer varchar(50) not null, 
	judet varchar(50) not null
)

create table TipuriLocuinte(
	tip varchar(50) primary key
)


create table Locuinte(
	id int primary key identity(1,1), 
	descriere varchar(50), 
	adresa varchar(100) not null, 
	telefon varchar(10), 
	tip varchar(50) foreign key references TipuriLocuinte(tip) not null, 
	curte bit not null, --0 nu are curte, 1 are
	o_id int foreign key references Orase(id)
)


create table Vizite(
	c_id int foreign key references Colindatori(id) not null, 
	l_id int foreign key references Locuinte(id) not null, 
	an int check(an > 0), 
	cantitate_dulciuri int check(cantitate_dulciuri > 0), 
	primary key(c_id, l_id, an)
)


insert into Trupe(nume, descriere, infiintare) values('Trupa1', 'Prima trupa', GETDATE())
insert into Colindatori(nume, prenume, mail, data_nastere, t_id) values
('ion', 'ion', 'ion@mail.mail', GETDATE(), 1), 
('marcelino', 'marcelion', 'marcelion@mail.mail', GETDATE(), 1)
insert into TipuriLocuinte(tip) values 
('apartament'), 
('vila'),
('beci'), 
('casa')

insert into Orase(numer, judet) values('Cluj-Napoca', 'Cluj'), ('Beclean pe SOmes', 'Bistrita-Nasaud')
insert into Locuinte(adresa, tip, curte, o_id) values 
('Strada Mare nr 1', 'vila', 1, 1),
('Strada Mare nr 2', 'apartament', 0, 1),
('Strada Mica nr 12', 'casa', 0, 2)





--2
go
create procedure SaveVizita 
	@c_id int,
	@l_id int,
	@an int, 
	@cantitate int
as
begin 
	if exists
	(	
		select * from Vizite v
		where v.c_id = @c_id and v.l_id = @l_id and v.an = @an
	) 
		begin
			update Vizite 
			set cantitate_dulciuri = @cantitate
			where c_id = @c_id and l_id = @l_id and an = @an 
		end
	else
		begin
			insert into Vizite(c_id, l_id, an, cantitate_dulciuri)
			values (@c_id, @l_id, @an, @cantitate)
		end
end

exec SaveVizita @c_id = 1, @l_id = 2, @an = 2024, @cantitate = 201
exec SaveVizita @c_id = 1, @l_id = 3, @an = 2025, @cantitate = 10000
select * from Vizite

-- 3

go
create view FilterColindatori as
select distinct
	c.nume, 
	c.prenume, 
	c.mail 
from Vizite v
inner join Colindatori c on v.c_id = c.id
inner join Locuinte l on l.id = v.l_id
group by c.nume, c.prenume, c.mail, l.tip
having (
		((sum(v.cantitate_dulciuri) > 100 and l.tip = 'vila')
		or (sum(v.cantitate_dulciuri) >= 200 and l.tip = 'apartament') and count(c.id) = 2))
		or ((sum(v.cantitate_dulciuri) > 100 and l.tip = 'vila') and count(c.id) = 1)
		

select * from Trupe
select * from Colindatori
select * from Orase
select * from Locuinte
select * from Vizite

select * from FilterColindatori

drop view FilterColindatori