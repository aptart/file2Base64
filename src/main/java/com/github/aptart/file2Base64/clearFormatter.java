package com.github.aptart.file2Base64;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class clearFormatter extends Formatter {
    @Override
    public String format(LogRecord record){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = format.format(new Date(record.getMillis()));
        return record.getLevel() + " " + time + " " + record.getSourceClassName() + " " + record.getSourceMethodName() + " : " + record.getMessage() + "\n";
    }
}
