package com.testehan.chatgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testehan.chatgpt.model.common.Message;
import com.testehan.chatgpt.model.request.RequestBody;
import com.testehan.chatgpt.model.common.Role;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonMain {
    public static void main(String[] args) throws IOException {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(Role.USER.name(), "Hello!"));
        RequestBody requestBody = new RequestBody("gpt-3.5-turbo",messages);

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(new File("target/car.json"), requestBody);
    }
}
