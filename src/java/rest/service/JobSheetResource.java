package rest.service;

import db.AttachmentDb;
import db.BusinessSystemDb;
import db.ClientDb;
import db.InquiryDb;
import db.JobSheetDb;
import db.UserDb;
import entity.Attachment;
import entity.BusinessSystem;
import entity.JobSheet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rest.dto.RestJobSheet;
import rest.dto.RestSearchConditionJobSheet;
import rest.dto.RestSearchJobSheet;
import rest.dto.RestStatJobSheet;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Path("/jobsheet")
public class JobSheetResource {
    @Inject
    JobSheetDb jobSheetDb;
    @Inject
    ClientDb clientDb;
    @Inject
    BusinessSystemDb businessSystemDb;
    @Inject
    InquiryDb inquiryDb;
    @Inject
    UserDb userDb;
    @Inject
    AttachmentDb attachmentDb;
    
    @Context
    ServletContext context;
    
    @POST
    @Path("/regist")
    @Consumes(MediaType.APPLICATION_JSON)
    public String registJobSheet(RestJobSheet restJobSheet) {
        try {
            // 新規登録のときはIDを生成する。
            if (StringUtils.isEmpty(restJobSheet.getId())) {
                String nextId = jobSheetDb.getNextId();
                restJobSheet.setId(nextId);
            }
            
            JobSheet jobSheet = new JobSheet();
            jobSheet.setId(restJobSheet.getId());
            jobSheet.setClient(clientDb.search(restJobSheet.getClient()));
            jobSheet.setBusinessSystem(businessSystemDb.search(restJobSheet.getBusinessSystem()));
            jobSheet.setInquiry(inquiryDb.search(restJobSheet.getInquiry()));
            jobSheet.setDepartment(restJobSheet.getDepartment());
            jobSheet.setPerson(restJobSheet.getPerson());
            if (!StringUtils.isEmpty(restJobSheet.getOccurDate()) && !StringUtils.isEmpty(restJobSheet.getOccurTime())) {
                String occurDateTime = restJobSheet.getOccurDate() + " " + restJobSheet.getOccurTime();
                jobSheet.setOccurDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(occurDateTime));
            }
            jobSheet.setTitle(restJobSheet.getTitle());
            jobSheet.setContent(restJobSheet.getContent());
            jobSheet.setContact(userDb.search(restJobSheet.getContact()));
            if (!StringUtils.isEmpty(restJobSheet.getLimitDate())) {
                jobSheet.setLimitDate(new SimpleDateFormat("yyyy-MM-dd").parse(restJobSheet.getLimitDate()));
            }
            jobSheet.setDeal(userDb.search(restJobSheet.getDeal()));
            if (!StringUtils.isEmpty(restJobSheet.getCompleteDate())) {
                jobSheet.setCompleteDate(new SimpleDateFormat("yyyy-MM-dd").parse(restJobSheet.getCompleteDate()));
            }
            jobSheet.setSupport(restJobSheet.getSupport());
            jobSheet.setResponseTime(restJobSheet.getResponseTime());
            
            if (jobSheetDb.search(restJobSheet.getId()) != null) {
                // 同じIDが登録済みの場合は更新する。
                jobSheetDb.update(jobSheet);
            } else {
                // 同じIDがない場合は登録する。
                jobSheetDb.add(jobSheet);
            }
            
            // IDを返却する。
            return restJobSheet.getId();
        } catch (ParseException ex) {
            throw new InternalServerErrorException();
        }
    }
    
    @DELETE
    @Path("/delete/{id}")
    public void deleteJobSheet(@PathParam("id") String id) {
        // 業務日誌を削除
        JobSheet target = jobSheetDb.search(id);
        if (target != null) {
            jobSheetDb.delete(target);
        }
        // 添付ファイルも削除する。
        List<Attachment> targetAttachmentList = attachmentDb.getFileList(id);
        targetAttachmentList.forEach(attachment -> {
            attachmentDb.delete(attachment);
        });
    }
    
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestSearchJobSheet> searchJobSheet(RestSearchConditionJobSheet condition) {
        try {
            System.out.println(condition);
            List<RestSearchJobSheet> restSearchJobSheetList = new ArrayList<>();
            List<JobSheet> jobSheetList =jobSheetDb.getJobSheetList(condition);
            for (JobSheet jobSheet: jobSheetList) {
                List<Attachment> attachmentList = attachmentDb.getFileList(jobSheet.getId());
                restSearchJobSheetList.add(new RestSearchJobSheet(jobSheet, attachmentList));
            }
            return restSearchJobSheetList;
        } catch (ParseException ex) {
            throw new InternalServerErrorException();
        }
    }
    
    @POST
    @Path("/download")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadJobSheet(RestSearchConditionJobSheet condition) {
        try {
            // 検索処理
            List<JobSheet> jobSheetList =jobSheetDb.getJobSheetList(condition);
            
            // Excel生成
            // テンプレートファイル名
            String templateName = "template.xlsx";
            // テンプレートファイルパスを指定
            String templatePath = context.getRealPath("resources/excel/" + templateName);
            Workbook wb = new XSSFWorkbook(templatePath);
            Sheet sheet = wb.getSheetAt(0);
            for (int rowIndex = 2; rowIndex <= jobSheetList.size() + 1; rowIndex++) {
                JobSheet jobSheet = jobSheetList.get(rowIndex - 2);
                Row row = sheet.getRow(rowIndex);
                if(row == null){
                    row = sheet.createRow(rowIndex);
                }
                // セルの枠線を設定する。
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                // 上詰め
                cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
                // 改行して表示
                cellStyle.setWrapText(true);
                // 日付用のセルスタイル
                CellStyle dateCellStyle = wb.createCellStyle();
                dateCellStyle.cloneStyleFrom(cellStyle);
                dateCellStyle.setDataFormat((short)0xe);
                // 日時用のセルスタイル
                CellStyle datetimeCellStyle = wb.createCellStyle();
                datetimeCellStyle.cloneStyleFrom(cellStyle);
                datetimeCellStyle.setDataFormat((short)0x16);
                
                // 番号
                setCellValue(row, 0, jobSheet.getId(), cellStyle);
                // 顧客
                setCellValue(row, 1, jobSheet.getClient().getName(), cellStyle);
                // 業務
                setCellValue(row, 2, jobSheet.getBusinessSystem().getBusiness().getName(), cellStyle);
                // システム
                setCellValue(row, 3, jobSheet.getBusinessSystem().getName(), cellStyle);
                // 問合せ区分
                setCellValue(row, 4, jobSheet.getInquiry().getName(), cellStyle);
                // 部署
                setCellValue(row, 5, jobSheet.getDepartment(), cellStyle);
                // 担当者
                setCellValue(row, 6, jobSheet.getPerson(), cellStyle);
                // 発生日時
                setCellValue(row, 7, jobSheet.getOccurDateTime(), datetimeCellStyle);
                // タイトル
                setCellValue(row, 8, jobSheet.getTitle(), cellStyle);
                // 内容
                setCellValue(row, 9, jobSheet.getContent(), cellStyle);
                // 窓口
                setCellValue(row, 10, jobSheet.getContact().getName(), cellStyle);
                // 対応者
                setCellValue(row, 11, jobSheet.getDeal().getName(), cellStyle);
                // 完了期限
                setCellValue(row, 12, jobSheet.getLimitDate(), dateCellStyle);
                // 完了日
                setCellValue(row, 13, jobSheet.getCompleteDate(), dateCellStyle);
                // 対応詳細
                setCellValue(row, 14, jobSheet.getSupport(), cellStyle);
                // 対応時間
                setCellValue(row, 15, jobSheet.getResponseTime(), cellStyle);
            }
            
            // レスポンスを生成する。
            String encodedFilename = URLEncoder.encode("台帳.xlsx", "UTF-8");
            Response.ResponseBuilder response = Response.ok(byteArray(wb));
            String headerVal = "attachment; filename=" + encodedFilename;
            response.header("Content-Disposition", headerVal);
            return response.build();
        } catch (ParseException | IOException ex) {
            throw new InternalServerErrorException();
        }
    }
    
    @GET
    @Path("/stats/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestStatJobSheet> getStatsJobSheet(@PathParam("year") String year) {
        List<RestStatJobSheet> restStatJobSheetList = new ArrayList<>();
        
        List<BusinessSystem> systemList = businessSystemDb.getAll();
        systemList.forEach(system -> {
            RestStatJobSheet restStatJobSheet = new RestStatJobSheet();
            restStatJobSheet.setBusinessSystem(system);
            // 1年の合計時間用
            Double responseTimeSum = 0.0;
            // 4月から3月まで件数をカウントする。
            for (int monthIdx = 1; monthIdx <= 12; monthIdx++) {
                int statsYear = Integer.parseInt(year);
                if (monthIdx <= 3) {
                    statsYear = statsYear + 1;
                }
                LocalDateTime ldFrom = LocalDateTime.of(statsYear, monthIdx, 1, 0, 0);
                TemporalAdjuster lastDay = TemporalAdjusters.lastDayOfMonth();
                LocalDateTime ldTo = ldFrom.with(lastDay).with(LocalTime.of(23, 59));
                
                Date from = Date.from(ldFrom.atZone(ZoneId.systemDefault()).toInstant());
                Date to = Date.from(ldTo.atZone(ZoneId.systemDefault()).toInstant());
                
                Integer occurCnt = jobSheetDb.statsOccur(system, from, to);
                Integer completeCnt = jobSheetDb.statsComplete(system, from, to);
                Double responseTime = jobSheetDb.sumTime(system, from, to);
                switch (monthIdx) {
                    case 1:
                        restStatJobSheet.setOccurCnt1(occurCnt);
                        restStatJobSheet.setCompleteCnt1(completeCnt);
                        restStatJobSheet.setResponseTime1(responseTime);
                        break;
                    case 2:
                        restStatJobSheet.setOccurCnt2(occurCnt);
                        restStatJobSheet.setCompleteCnt2(completeCnt);
                        restStatJobSheet.setResponseTime2(responseTime);
                        break;
                    case 3:
                        restStatJobSheet.setOccurCnt3(occurCnt);
                        restStatJobSheet.setCompleteCnt3(completeCnt);
                        restStatJobSheet.setResponseTime3(responseTime);
                        break;
                    case 4:
                        restStatJobSheet.setOccurCnt4(occurCnt);
                        restStatJobSheet.setCompleteCnt4(completeCnt);
                        restStatJobSheet.setResponseTime4(responseTime);
                        break;
                    case 5:
                        restStatJobSheet.setOccurCnt5(occurCnt);
                        restStatJobSheet.setCompleteCnt5(completeCnt);
                        restStatJobSheet.setResponseTime5(responseTime);
                        break;
                    case 6:
                        restStatJobSheet.setOccurCnt6(occurCnt);
                        restStatJobSheet.setCompleteCnt6(completeCnt);
                        restStatJobSheet.setResponseTime6(responseTime);
                        break;
                    case 7:
                        restStatJobSheet.setOccurCnt7(occurCnt);
                        restStatJobSheet.setCompleteCnt7(completeCnt);
                        restStatJobSheet.setResponseTime7(responseTime);
                        break;
                    case 8:
                        restStatJobSheet.setOccurCnt8(occurCnt);
                        restStatJobSheet.setCompleteCnt8(completeCnt);
                        restStatJobSheet.setResponseTime8(responseTime);
                        break;
                    case 9:
                        restStatJobSheet.setOccurCnt9(occurCnt);
                        restStatJobSheet.setCompleteCnt9(completeCnt);
                        restStatJobSheet.setResponseTime9(responseTime);
                        break;
                    case 10:
                        restStatJobSheet.setOccurCnt10(occurCnt);
                        restStatJobSheet.setCompleteCnt10(completeCnt);
                        restStatJobSheet.setResponseTime10(responseTime);
                        break;
                    case 11:
                        restStatJobSheet.setOccurCnt11(occurCnt);
                        restStatJobSheet.setCompleteCnt11(completeCnt);
                        restStatJobSheet.setResponseTime11(responseTime);
                        break;
                    case 12:
                        restStatJobSheet.setOccurCnt12(occurCnt);
                        restStatJobSheet.setCompleteCnt12(completeCnt);
                        restStatJobSheet.setResponseTime12(responseTime);
                        break;
                }
                // 1年の合計時間に加算
                responseTimeSum = responseTimeSum + responseTime;
            }
            // 未完了の件数を求める。
            Integer leftCnt = jobSheetDb.countLeft(system);
            restStatJobSheet.setLeftCnt(leftCnt);
            // 1年の合計時間
            restStatJobSheet.setResponseTimeSum(responseTimeSum);
            // 結果リストに追加
            restStatJobSheetList.add(restStatJobSheet);
        });
        
        return restStatJobSheetList;
    }
    
    @GET
    @Path("/stats/{year}/{systemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestStatJobSheet getChart(@PathParam("year") Integer year, @PathParam("systemId") Integer systemId) {
        BusinessSystem targetSystem = businessSystemDb.search(systemId);
        if (targetSystem != null) {
            RestStatJobSheet restStatJobSheet = new RestStatJobSheet();
            restStatJobSheet.setBusinessSystem(targetSystem);
            // 4月から3月まで件数をカウントする。
            for (int monthIdx = 1; monthIdx <= 12; monthIdx++) {
                int statsYear = year;
                if (monthIdx <= 3) {
                    statsYear = statsYear + 1;
                }
                LocalDateTime ldFrom = LocalDateTime.of(statsYear, monthIdx, 1, 0, 0);
                TemporalAdjuster lastDay = TemporalAdjusters.lastDayOfMonth();
                LocalDateTime ldTo = ldFrom.with(lastDay).with(LocalTime.of(23, 59));
                
                Date from = Date.from(ldFrom.atZone(ZoneId.systemDefault()).toInstant());
                Date to = Date.from(ldTo.atZone(ZoneId.systemDefault()).toInstant());
                
                Integer occurCnt = jobSheetDb.statsOccur(targetSystem, from, to);
                Integer completeCnt = jobSheetDb.statsComplete(targetSystem, from, to);
                
                switch (monthIdx) {
                    case 1:
                        restStatJobSheet.setOccurCnt1(occurCnt);
                        restStatJobSheet.setCompleteCnt1(completeCnt);
                        break;
                    case 2:
                        restStatJobSheet.setOccurCnt2(occurCnt);
                        restStatJobSheet.setCompleteCnt2(completeCnt);
                        break;
                    case 3:
                        restStatJobSheet.setOccurCnt3(occurCnt);
                        restStatJobSheet.setCompleteCnt3(completeCnt);
                        break;
                    case 4:
                        restStatJobSheet.setOccurCnt4(occurCnt);
                        restStatJobSheet.setCompleteCnt4(completeCnt);
                        break;
                    case 5:
                        restStatJobSheet.setOccurCnt5(occurCnt);
                        restStatJobSheet.setCompleteCnt5(completeCnt);
                        break;
                    case 6:
                        restStatJobSheet.setOccurCnt6(occurCnt);
                        restStatJobSheet.setCompleteCnt6(completeCnt);
                        break;
                    case 7:
                        restStatJobSheet.setOccurCnt7(occurCnt);
                        restStatJobSheet.setCompleteCnt7(completeCnt);
                        break;
                    case 8:
                        restStatJobSheet.setOccurCnt8(occurCnt);
                        restStatJobSheet.setCompleteCnt8(completeCnt);
                        break;
                    case 9:
                        restStatJobSheet.setOccurCnt9(occurCnt);
                        restStatJobSheet.setCompleteCnt9(completeCnt);
                        break;
                    case 10:
                        restStatJobSheet.setOccurCnt10(occurCnt);
                        restStatJobSheet.setCompleteCnt10(completeCnt);
                        break;
                    case 11:
                        restStatJobSheet.setOccurCnt11(occurCnt);
                        restStatJobSheet.setCompleteCnt11(completeCnt);
                        break;
                    case 12:
                        restStatJobSheet.setOccurCnt12(occurCnt);
                        restStatJobSheet.setCompleteCnt12(completeCnt);
                        break;
                }
            }
            return restStatJobSheet;
        } else {
            throw new NotFoundException();
        }
    }
    
    // セルに値を設定
    private void setCellValue(Row row, int cellIndex, Object value, CellStyle cellStyle) {
        Cell cell = row.getCell(cellIndex);
        if(cell == null){
            cell = row.createCell(cellIndex);
        }
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Number) {
                Number numValue = (Number) value;
                if (numValue instanceof Float) {
                    Float floatValue = (Float) numValue;
                    numValue = new Double(String.valueOf(floatValue));
                }
                cell.setCellValue(numValue.doubleValue());
            } else if (value instanceof Date) {
                Date dateValue = (Date) value;
                cell.setCellValue(dateValue);
            } else if (value instanceof Boolean) {
                Boolean boolValue = (Boolean) value;
                cell.setCellValue(boolValue);
            }
        }
    }
    
    /**
    * ワークブックからbyte配列取得
    */
    private byte[] byteArray(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new IOException();
        }
   }
}
