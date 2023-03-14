package com.testehan.chatgpt.model.response;

import java.util.List;

public class ResponseBody {

    private String id;
    private String object;
    private long created;               // seconds since 1970

    private String model;
    private List<ChatReply> choices;
    private ResponseUsage usage;

    public ResponseBody(){}

    public ResponseBody(String id, String object, long created, String model, List<ChatReply> choices, ResponseUsage usage) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public List<ChatReply> getChoices() {
        return choices;
    }

    public ResponseUsage getUsage() {
        return usage;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model=" + model +
                ", choices=" + choices +
                ", usage=" + usage +
                '}';
    }
}
