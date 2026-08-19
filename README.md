# Band Rehearsal Manager - Backend

REST API za upravljanje bendovima, muzičarima, probama i salama za probe.
Završni projekat iz predmeta Internet softverske arhitekture koji je dodeljen kao diferencijalni predmet za master na smeru Nauka o podacima.

## Tehnologije
- Java 21
- Spring Boot (REST)
- Spring Data JPA / Hibernate
- MySQL (MariaDB kompatibilno)
- Gradle

## Struktura
Slojevita arkhitektura: Controller -> Service -> Repository.
Pet tabela: bands, musicians, rehearsals, rehearsal_rooms, attendances.
(+ spojna tabela band_musicians za ManyToMany vezu)
Relacije: OneToMany (Band -> Rehearsal, Rehearsal Room -> Rehearsal, Musician -> Attendance, Rehearsal -> Attendance),
ManyToMany (Musician <-> Band).

## Pokretanje
1. Pokrenuti MySQL server (npr. XAMPP).
2. Uvesti bazu iz priložene skripte 'band_manager.sql' (kreira bazu 'band_manager', tabele i test podatke).
3. Po potrebi izmeniti pristupne podatke u 'src/main/resources/application.properties.
4. Pokrenuti aplikaciju:
   ./gradlew bootRun
5. API je dostupan na http://localhost:8080

## Dokumentacija
Postman dokumentacija sa primerima se nalazi na sledećoj adresi:
https://documenter.getpostman.com/view/57528690/2sBYArSrm2

## Frontend
Dostupan na sledećoj adresi(javni pristup):
https://github.com/buka-buka-glasnost/band-rehearsal-frontend



  


