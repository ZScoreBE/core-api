package be.zsoft.zscore.core.service.utils;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PebbleServiceTest {

    @Mock
    private PebbleEngine pebbleEngine;
    @Mock
    private PebbleTemplate pebbleTemplate;
    @Mock
    private WriterFactory writerFactory;
    @Mock
    private StringWriter writer;

    @InjectMocks
    private PebbleService pebbleService;

    @Test
    void generateHTML() throws Exception {
        when(pebbleEngine.getTemplate("/hello/file.peb")).thenReturn(pebbleTemplate);
        when(writerFactory.getStringWriter()).thenReturn(writer);
        when(writer.toString()).thenReturn("generated html");

        String result = pebbleService.generateHTML("/hello/file.peb", Map.of("var1", "value1"));

        assertEquals("generated html", result);

        verify(pebbleTemplate).evaluate(writer, Map.of("var1", "value1"));
    }
}