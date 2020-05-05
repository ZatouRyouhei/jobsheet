package rest.service;

import db.AttachmentDb;
import entity.Attachment;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import rest.dto.RestAttachment;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Path("/attachment")
public class AttachmentResource {
    @Inject
    AttachmentDb attachmentDb;
    
    @POST
    @Path("/regist/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestAttachment> registAttachment(@PathParam("id") String id, @FormDataParam("file") List<FormDataBodyPart> bodyParts) {
        for (FormDataBodyPart part : bodyParts) {
            // 添付ファイル登録
            attachmentDb.registAttachment(id, part);
        }
        // 返信用の添付ファイルリストを作成
        List<Attachment> attachmentList = attachmentDb.getFileList(id);
        List<RestAttachment> restAttachmentList = attachmentList
                                                    .stream()
                                                    .map(attachment -> new RestAttachment(attachment))
                                                    .collect(Collectors.toList());
        restAttachmentList.sort(Comparator.comparing(RestAttachment::getSeqNo));
        return restAttachmentList;
    }
    
    @GET
    @Path("/download/{id}/{seqNo}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadAttachment(@PathParam("id") String id, @PathParam("seqNo") Integer seqNo) {
        try {
            Attachment file = attachmentDb.getFile(id, seqNo);
            
            ResponseBuilder response = Response.ok(file.getAttachFile());
            String encodedFilename = URLEncoder.encode(file.getFileName(), "UTF-8");
            String headerVal = "attachment; filename=" + encodedFilename;
            response.header("Content-Disposition", headerVal);
            return response.build();
        } catch (UnsupportedEncodingException ex) {
            throw new NotFoundException();
        }
    }
    
    @DELETE
    @Path("/delete/{id}/{seqNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestAttachment> deleteAttachment(@PathParam("id") String id, @PathParam("seqNo") Integer seqNo) {
        Attachment file = attachmentDb.getFile(id, seqNo);
        if (file != null) {
            attachmentDb.delete(file);
        } else {
            throw new NotFoundException();
        }
        
        // 返信用の添付ファイルリストを作成
        List<Attachment> attachmentList = attachmentDb.getFileList(id);
        List<RestAttachment> restAttachmentList = attachmentList
                                                    .stream()
                                                    .map(attachment -> new RestAttachment(attachment))
                                                    .collect(Collectors.toList());
        restAttachmentList.sort(Comparator.comparing(RestAttachment::getSeqNo));
        return restAttachmentList;
    }
}
