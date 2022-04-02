package rest.service;

import db.HolidayDb;
import entity.Holiday;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import rest.dto.RestHoliday;

/**
 *
 * @author ryouhei
 */
@RequestScoped
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
}
