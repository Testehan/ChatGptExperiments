package com.testehan.chatgpt.model.response;

import com.testehan.chatgpt.model.common.Message;

// entries of this type are returned inside the Response object under an element called "choices"
// so maybe I should change the name of this to Choice ...still the current name is easier to understand imo
public class ChatReply {

    private int index;
    private Message message;
    private String finish_reason;

    public ChatReply(){}

    public ChatReply(int index, Message message, String finish_reason) {
        this.index = index;
        this.message = message;
        this.finish_reason = finish_reason;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getFinish_reason() {
        return finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }

    @Override
    public String toString() {
        return "ChatReply{" +
                "index=" + index +
                ", message=" + message +
                ", finish_reason='" + finish_reason + '\'' +
                '}';
    }
}
