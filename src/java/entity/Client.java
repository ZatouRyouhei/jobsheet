package entity;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 顧客
 * @author ryouhei
 */
@Entity
@Table(name="t_client")
@Cacheable(false)
public class Client implements Serializable  {
    public static final int SIZE_ID = 20;
    
    @Id
    private Integer id;
    @Column(length=SIZE_ID)
    private String name;

    public Client() {
    }

    public Client(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
