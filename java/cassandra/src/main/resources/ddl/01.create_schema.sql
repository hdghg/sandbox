CREATE TABLE kn.vet (
  id            uuid,
  firstName     text,
  lastName      text,
  specialties   set<text>,
  PRIMARY KEY (id));

CREATE TABLE kn.vet_by_clinic (
  clinicName    text,
  vetId         uuid,
  fullName      text,
  PRIMARY KEY ((clinicName), fullName))
  WITH comment = 'Q1. Find vets by clinic'
  AND CLUSTERING ORDER BY (fullName ASC);
