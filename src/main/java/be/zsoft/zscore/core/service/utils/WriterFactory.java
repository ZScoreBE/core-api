package be.zsoft.zscore.core.service.utils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;

@Service
public class WriterFactory {

    public StringWriter getStringWriter() throws IOException {
        return new StringWriter();
    }
}
