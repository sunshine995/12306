package com.jiawa.train.business.req;

import com.jiawa.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DailyTrainStationQueryReq extends PageReq {


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String trainCode;

    @Override
    public String toString() {
        return "DailyTrainStationQueryReq{" +
                "} " + super.toString();
    }
}
