package com.testehan.chatgpt.model.request;

import com.testehan.chatgpt.model.common.Message;

import java.util.List;

public class RequestBody {

    private String model = "gpt-3.5-turbo";
    private List<Message> messages;

    public RequestBody(String model, List<Message> messages) {
        this.model=model;
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getModel() {
        return model;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
