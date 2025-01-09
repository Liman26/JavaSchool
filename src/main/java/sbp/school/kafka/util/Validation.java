package sbp.school.kafka.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import sbp.school.kafka.serializer.TransactionSerializer;

import java.util.Set;
import java.util.stream.Collectors;

public class Validation {

    public static void validateWithSchema(JsonNode data) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema(
                TransactionSerializer.class.getResourceAsStream("/transaction.json")
        );
        Set<ValidationMessage> messages = jsonSchema.validate(data);
        if (!messages.isEmpty()) {
            throw new RuntimeException("Ошибка при валидации по схеме: " +
                    String.join(
                            ";",
                            messages.stream().map(ValidationMessage::getMessage)
                                    .collect(Collectors.toSet())
                    )
            );
        }
    }
}
