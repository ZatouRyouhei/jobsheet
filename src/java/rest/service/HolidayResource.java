package rest.service;

import db.HolidayDb;
import entity.Holiday;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import rest.dto.RestErrorMsg;
import rest.dto.RestHoliday;
import rest.filter.Authenticate;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Authenticate
@Path("/holiday")
public class HolidayResource {
    @Inject
    HolidayDb holidayDb;
    
    @GET
    @Path("/getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestHoliday> getList() {
        List<Holiday> holidayList = holidayDb.getAll();
        List<RestHoliday> restHolidayList = holidayList
                                        .stream()
                                        .map(holiday -> new RestHoliday(holiday.getHoliday(), holiday.getName()))
                                        .sorted(Comparator.comparing(RestHoliday::getHoliday).reversed())
                                        .collect(Collectors.toList());
        return restHolidayList;
    }
    
    /**
     * 祝日テーブル登録
     * @param bodyPart
     * @return エラーリスト
     */
    @POST
    @Path("/regist")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestErrorMsg> registHoliday(@FormDataParam("file") FormDataBodyPart bodyPart) {
        // ファイルは内閣府のホームページに公開されているCSVファイルの想定
        // 文字コードはSJIS
        // ヘッダーあり
        // ダブルクォーテーションなし
        List<RestErrorMsg> errorList = new ArrayList<>();  // エラーメッセージのリスト
        try {
            InputStream inputStream = bodyPart.getValueAs(InputStream.class);
            List<String> holidayList = IOUtils.readLines(inputStream, "Windows-31J");
            
            // ヘッダーを含めて２行以上あること
            if (holidayList.size() < 2) {
                RestErrorMsg error = new RestErrorMsg(0, "ヘッダーを含め2行以上入力してください。");
                errorList.add(error);
            } else {
                // エラーチェック ヘッダー行は無視する
                for (int i = 1; i < holidayList.size(); i++) {
                    String[] holidayData = holidayList.get(i).split(",");
                    if (holidayData.length != 2) {
                        // 項目数が2つではないときエラーとして次の行に行く
                        RestErrorMsg error = new RestErrorMsg(i+1, "フォーマットエラー（日付と祝日名称を入力してください。）");
                        errorList.add(error);
                        continue;
                    }
                    if (holidayData[1].length() > 20) {
                        // 祝日名称が20文字を超えている場合はエラーとする。
                        RestErrorMsg error = new RestErrorMsg(i+1, "祝日名称エラー（祝日名称は20文字以内としてください。）");
                        errorList.add(error);
                        continue;
                    }
                    try {
                        // 日付変換ができるかどうかチェック
                        new SimpleDateFormat("yyyy/MM/dd").parse(holidayData[0]);
                    } catch (ParseException ex) {
                        // 日付変換ができないときはエラーとして次の行に行く
                        RestErrorMsg error = new RestErrorMsg(i+1, "日付形式のエラー（yyyy/mm/ddとしてください。）");
                        errorList.add(error);
                        continue;
                    }
                }
            }
            
            
            // エラーがないときは登録処理を実施
            if (errorList.isEmpty()) {
                // 祝日テーブルを空にする。
                holidayDb.deleteAll();
                // 祝日テーブルに登録
                for (int i = 1; i < holidayList.size(); i++) {
                    String[] holidayData = holidayList.get(i).split(",");
                    try {
                        Date targetDate = new SimpleDateFormat("yyyy/MM/dd").parse(holidayData[0]);
                        String targetName = holidayData[1];
                        Holiday holiday = new Holiday(targetDate, targetName);
                        holidayDb.add(holiday);
                    } catch (ParseException ex) {
                        // 前段でエラーチェックをしているので、ここには来ないはず。
                    }
                }
            }
            
        } catch (IOException ex) {
            RestErrorMsg error = new RestErrorMsg(0, "ファイルを読み込めませんでした。");
            errorList.add(error);
        }
        
        return errorList;
    }
}
