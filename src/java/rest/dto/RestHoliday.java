package rest.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestHoliday {
    private String holiday;  // 日付 yyyy-MM-dd
    private String name;

    public RestHoliday() {
    }

    public RestHoliday(String holiday, String name) {
        this.holiday = holiday;
        this.name = name;
    }
    
    public RestHoliday(Date holiday, String name) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  // 日付を文字列に変換
        this.holiday = df.format(holiday);
        this.name = name;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
