package entity;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * ユーザ
 * @author ryouhei
 */
@NamedQueries({
    @NamedQuery (
            name = User.USER_GETMAXSEQ,
            query =   "SELECT MAX(u.seqNo)"
                    + "  FROM User u"
    ),
    @NamedQuery (
            name = User.USER_LOGIN,
            query =   "SELECT u"
                    + "  FROM User u"
                    + " WHERE u.id = :userId"
                    + "   AND u.password = :password"
    )
})
@Entity
@Table(name="t_user")
@Cacheable(false)
public class User implements Serializable {
    public static final String USER_GETMAXSEQ = "USER_GETMAXSEQ";
    public static final String USER_LOGIN = "USER_LOGIN";
    
    public static final int SIZE_ID = 20;
    public static final int SIZE_PASSWORD = 128;
    public static final int SIZE_NAME = 20;
    
    @Id
    @Column(length=SIZE_ID)
    private String id;
    @Column(length=SIZE_PASSWORD)
    private String password;
    @Column(length=SIZE_NAME)
    private String name;
    private Integer seqNo;

    public User() {
    }

    public User(String id, String password, String name, Integer seqNo) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.seqNo = seqNo;
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

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
}
