package be.zsoft.zscore.core.service.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class WriterFactoryTest {

    @InjectMocks
    private WriterFactory writerFactory;

    @Test
    void getStringWriter() throws Exception {
        assertNotNull(writerFactory.getStringWriter());
    }
}