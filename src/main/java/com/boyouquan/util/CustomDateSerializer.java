package com.boyouquan.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;

import java.io.IOException;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
        gen.writeString(CommonUtils.dateHourSecondCommonFormatDisplay(date));
    }

}