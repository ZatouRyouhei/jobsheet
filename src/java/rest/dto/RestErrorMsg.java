package rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * csv一括登録のエラーメッセージ
 * @author ryouhei
 */
@XmlRootElement
public class RestErrorMsg {
    private Integer lineNo;
    private String errorMsg;

    public RestErrorMsg() {
    }

    public RestErrorMsg(Integer lineNo, String errorMsg) {
        this.lineNo = lineNo;
        this.errorMsg = errorMsg;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
