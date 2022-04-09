package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 休日テーブル
 * @author ryouhei
 */
@NamedQueries({
    @NamedQuery (
            name = Holiday.HOLIDAY_DELETE_ALL,
            query = "DELETE FROM Holiday"
    )
})
@Entity
@Table(name="t_holiday")
@Cacheable(false)
public class Holiday implements Serializable {
    public static final String HOLIDAY_DELETE_ALL = "HOLIDAY_DELETE_ALL";
    public static final int SIZE_NAME = 20;
    
    @Id
    @Temporal(TemporalType.DATE)
    private Date holiday;
    @Column(length=SIZE_NAME)
    private String name;

    public Holiday() {
    }

    public Holiday(Date holiday, String name) {
        this.holiday = holiday;
        this.name = name;
    }

    public Date getHoliday() {
        return holiday;
    }

    public void setHoliday(Date holiday) {
        this.holiday = holiday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
