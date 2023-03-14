package com.testehan.chatgpt;

import com.testehan.chatgpt.model.common.Message;
import com.testehan.chatgpt.model.common.Role;
import com.testehan.chatgpt.model.request.RequestBody;
import com.testehan.chatgpt.model.response.ChatReply;
import com.testehan.chatgpt.model.response.ResponseBody;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 *  Simple chatGPT console app
 *
 */
public class ConsoleGPT
{
    public static void main( String[] args )
    {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.openai.com/v1/chat/completions");
        Response response = null;

        List<Message> messages = new ArrayList<>();
        RequestBody requestBody = new RequestBody("gpt-3.5-turbo",messages);

        String myReply = "hi chatGPT";
        System.out.println("Dan: "+ myReply);
        while (!myReply.equalsIgnoreCase("exitgpt")) {
            messages.add(new Message(Role.USER.name().toLowerCase(), myReply));

            response = postGPT(requestBody, target);
            ResponseBody responseBody = response.readEntity(ResponseBody.class);
            ChatReply gptLatestReply = responseBody.getChoices().get(responseBody.getChoices().size()-1);

            System.out.println("GPT: " + gptLatestReply.getMessage().getContent());
            messages.add(gptLatestReply.getMessage());

            System.out.print("Dan: ");
            Scanner scanner = new Scanner(System.in);
            myReply = scanner.nextLine();
        }

        if (response != null) {
            response.close();
        }
        if (client != null) {
            client.close();
        }

    }

    private static Response postGPT(RequestBody requestBody, WebTarget target) {
        Response response = target.request()
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .header("Authorization",getAuthorizationValue())
                        .post(Entity.json(requestBody));
        return response;
    }

    private static String getAuthorizationValue(){
        Properties prop = new Properties();
        try {
            prop.load(ConsoleGPT.class.getResourceAsStream("/secret.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.get("Authorization").toString();
    }
}
