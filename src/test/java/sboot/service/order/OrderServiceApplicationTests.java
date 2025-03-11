package sboot.service.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
class OrderServiceApplicationTests {

	@Test
	void contextLoads() {

	}
}
