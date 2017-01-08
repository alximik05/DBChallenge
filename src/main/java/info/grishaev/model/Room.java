package info.grishaev.model;

import javax.persistence.*;

/**
 * Created by stas on 07/01/17.
 */

@Entity
@Table(name = "ROOM")
public class Room {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    int id;

    @Column(name = "room_name")
    String roomName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
