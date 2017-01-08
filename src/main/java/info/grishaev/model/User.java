package info.grishaev.model;

import javax.persistence.*;

/**
 * Created by stas on 07/01/17.
 */

@Entity
@Table(name = "\"USER\"")
public class User {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "room_number")
//    @OneToOne(targetEntity = Room.class)
    int roomNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
