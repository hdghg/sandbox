CREATE TABLE spring_cassandra.vet (
  id            uuid,
  firstName     text,
  lastName      text,
  specialties   set<text>,
  PRIMARY KEY (id));
