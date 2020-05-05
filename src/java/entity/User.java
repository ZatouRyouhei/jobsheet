package entity;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ユーザ
 * @author ryouhei
 */
@Entity
@Table(name="t_user")
@Cacheable(false)
public class User implements Serializable {
    public static final int SIZE_ID = 20;
    public static final int SIZE_PASSWORD = 5;
    public static final int SIZE_NAME = 20;
    
    @Id
    @Column(length=SIZE_ID)
    private String id;
    @Column(length=SIZE_PASSWORD)
    private String password;
    @Column(length=SIZE_NAME)
    private String name;

    public User() {
    }

    public User(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
