package com.example.tasklet;

import com.example.component.SampleComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.repeat.RepeatStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
@DisplayName("tasklet2 unit test")
class Tasklet2Test {

    @InjectMocks
    private Tasklet2 tasklet2;

    @Mock
    private SampleComponent component;

    @BeforeAll
    static void beforeAll() {
        log.info("start tasklet2 unit test");
    }

    @AfterAll
    static void afterAll() {
        log.info("end tasklet2 unit test");
    }

    @Test
    @DisplayName("RepeatStatus is FINISHED")
    void shouldFinished() throws Exception {
        RepeatStatus repeatStatus = tasklet2.execute(null, null);
        assertThat(repeatStatus).isNotNull();
        assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
    }

    @Test
    @Disabled("Random value is 10")
    void checkRandomValue() throws Exception {
        when(component.random()).thenReturn(10);
        tasklet2.execute(null, null);

        assertThat(tasklet2.getRandomValue()).isEqualTo(10);
    }
}