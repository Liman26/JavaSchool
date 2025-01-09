package sbp.school.kafka.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sbp.school.kafka.model.Transaction;
import sbp.school.kafka.util.Validation;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionSerializer implements Serializer<Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionSerializer.class);

    /**
     * метод сериализует объект для его дальнейшей отправки в Kafka
     * @param s - не используется в данной реализации метода
     * @param transaction - сериализуемый объект
     * @return массив байт-кода
     */

    @Override
    public byte[] serialize(String s, Transaction transaction) {

        if (transaction != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
            objectMapper.registerModule(new JavaTimeModule());

            try {
                String value = objectMapper.writeValueAsString(transaction);
                Validation.validateWithSchema(objectMapper.readTree(value));
                return value.getBytes(StandardCharsets.UTF_8);
            } catch (JsonProcessingException e) {
                log.error("Ошибка в serialize: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return new byte[0];
    }
}
