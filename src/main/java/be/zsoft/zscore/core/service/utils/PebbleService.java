package be.zsoft.zscore.core.service.utils;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PebbleService {

    private final PebbleEngine pebbleEngine;
    private final WriterFactory writerFactory;

    public String generateHTML(String absolutePath, Map<String, Object> templateVars) throws IOException {
        log.info("Generating HTML from: {}", absolutePath);

        PebbleTemplate pebbleTemplate = pebbleEngine.getTemplate(absolutePath);
        StringWriter writer = writerFactory.getStringWriter();
        pebbleTemplate.evaluate(writer, templateVars);

        return writer.toString();
    }
}
