package com.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONMapper  implements MapperInterface {

    @Override
    public String map(Object obj) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // Handle the exception, or rethrow as a RuntimeException
            e.printStackTrace();
            return "{}"; // Or some other default value
        }

    }
}
