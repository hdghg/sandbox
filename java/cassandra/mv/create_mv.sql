insert into vet_by_clinic (clinicName, fullName) values ('DrSlon', 'Alex Turner');
insert into vet_by_clinic (clinicName, fullName) values ('DrSlon', 'Sara Conor');
insert into vet_by_clinic (clinicName, fullName) values ('DrSlon', 'Bill Gates');
insert into vet_by_clinic (clinicName, fullName) values ('Pup', 'Alex Turner');
insert into vet_by_clinic (clinicName, fullName) values ('Pup', 'Arnold Shwartsneger');

CREATE MATERIALIZED VIEW clinic_by_vet AS
   SELECT fullName, clinicName FROM vet_by_clinic
   WHERE fullName IS NOT NULL AND clinicName IS NOT NULL
   PRIMARY KEY ((fullName), clinicName);
