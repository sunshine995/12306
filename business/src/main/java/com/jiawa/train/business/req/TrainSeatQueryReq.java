package com.jiawa.train.business.req;

import com.jiawa.train.common.req.PageReq;
import lombok.Data;

@Data
public class TrainSeatQueryReq extends PageReq {
    private String trainCode;

    @Override
    public String toString() {
        return "TrainSeatQueryReq{" +
                "} " + super.toString();
    }
}
