use [PRPRACTIC2]

create table Producatori(
	id int primary key identity(1,1), 
	nume varchar(50) not null, 
	webstie varchar(50) not null, 
	phone varchar(10) not null
)

create table Pateuri(
	id int primary key identity(1,1), 
	denumire varchar(50) not null, 
	descriere varchar(250) not null, 
	kcal int check(kcal > 0) not null, 
	greutate int check(greutate > 0) not null, 
	data_lansare date not null, 
	prod_id int foreign key references Producatori(id)
)

create table Evenimente(
	id int primary key identity(1,1), 
	nume varchar(50) not null, 
	data datetime not null, 
	adresa varchar(50) not null, 
	sponsor_principal varchar(15) not null
)

create table Pariticipanti(
	id int primary key identity(1,1),
	name varchar(50)
	-- etc
)
create table Reviews(
	pt_id int foreign key references Pateuri(id),
	pr_id int foreign key references Pariticipanti(id),
	e_id int foreign key references Evenimente(id), 
	primary key(pt_id, pr_id, e_id)
)

--2

create procedure SaveReview
as
	@
begin
	if exists (
		select *
	)