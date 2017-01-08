DROP TABLE "USER";
DROP TABLE ROOM;
DELETE FROM "USER";
DELETE FROM ROOM;


CREATE TABLE IF NOT EXISTS ROOM (
  id        IDENTITY PRIMARY KEY,
  room_name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS "USER" (
id          IDENTITY PRIMARY KEY,
name        VARCHAR(255),
room_number INTEGER ,
FOREIGN KEY (room_number) REFERENCES room(id) ON DELETE CASCADE
);