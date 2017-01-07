DROP TABLE "user";
DROP TABLE room;
DELETE FROM "user";
DELETE FROM room;



CREATE TABLE IF NOT EXISTS room (
  id        INT PRIMARY KEY,
  room_name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS "user" (
id          INT PRIMARY KEY,
name        VARCHAR(255),
room_number INTEGER ,
FOREIGN KEY (room_number) REFERENCES room(id) ON DELETE CASCADE
);