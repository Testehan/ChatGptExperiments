package com.testehan.chatgpt.model.response;

public class ResponseUsage {

    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;

    public ResponseUsage(){}

    public ResponseUsage(int prompt_tokens, int completion_tokens, int total_tokens) {
        this.prompt_tokens = prompt_tokens;
        this.completion_tokens = completion_tokens;
        this.total_tokens = total_tokens;
    }

    public int getPrompt_tokens() {
        return prompt_tokens;
    }

    public int getCompletion_tokens() {
        return completion_tokens;
    }

    public int getTotal_tokens() {
        return total_tokens;
    }

    @Override
    public String toString() {
        return "ResponseUsage{" +
                "prompt_tokens=" + prompt_tokens +
                ", completion_tokens=" + completion_tokens +
                ", total_tokens=" + total_tokens +
                '}';
    }
}
