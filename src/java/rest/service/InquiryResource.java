package rest.service;

import db.InquiryDb;
import entity.Inquiry;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import rest.dto.RestInquiry;
import rest.filter.Authenticate;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Authenticate
@Path("/inquiry")
public class InquiryResource {
    @Inject
    InquiryDb inquiryDb;
    
    @GET
    @Path("/getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestInquiry> getList() {
        List<Inquiry> inquiryList = inquiryDb.getAll();
        List<RestInquiry> restInquiryList = inquiryList
                                                .stream()
                                                .map(inquiry -> new RestInquiry(inquiry))
                                                .collect(Collectors.toList());
        restInquiryList.sort(Comparator.comparing(RestInquiry::getId));
        return restInquiryList;
    }
}
