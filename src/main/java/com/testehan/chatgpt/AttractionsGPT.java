package com.testehan.chatgpt;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 *  Simple chatGPT console app
 *
 */
public class AttractionsGPT {

    private static final String CITY = "Rome";
    private static final int NUMBER_OF_ATTRACTIONS = 2;
    private static final int NUMBER_OF_CHAPTERS = 3;
    private static final int MAXIMUM_NO_OF_WORDS_PER_CHAPTER = 150;  // 500 should be like max

    private static final String QUESTION_1 = "You are a tourist guide. Give me a list of names for the " + NUMBER_OF_ATTRACTIONS +
            " most important tourist attractions in " + CITY + ". Your response must be in JSON format " +
            "with 1 parameters 'attractions'. No other information should be present in the reply. Only the names of the attractions.";
    private static final String QUESTION_2 = "You are a tourist guide. For attraction %s give me a list of " +
            NUMBER_OF_CHAPTERS + " chapter titles.Your response must be in JSON format with 1 parameters 'chapters'. Only the title " +
            "of the chapters must be present in the JSON response. No other information should be present in the reply. This is very important.";
    private static final String QUESTION_3 = "You are a tourist guide. For attraction '%s' for chapter '%s' give me a short essay of maximum " + MAXIMUM_NO_OF_WORDS_PER_CHAPTER +
            " words.Your response must be in JSON format with 1 parameters 'description'";

    public static void main( String[] args ) throws JsonProcessingException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.openai.com/v1/chat/completions");
        Response response = null;

        List<Message> messages = new ArrayList<>();
        RequestBody requestBody = new RequestBody("gpt-3.5-turbo",messages);

        String myReply = "hi chatGPT";
        System.out.println("Dan: "+ myReply);

        messages.add(new Message(Role.USER.name().toLowerCase(), myReply));

        response = postGPT(requestBody, target);
        ResponseBody responseBody = response.readEntity(ResponseBody.class);
        ChatReply gptLatestReply = responseBody.getChoices().get(responseBody.getChoices().size()-1);

        System.out.println("GPT: " + gptLatestReply.getMessage().getContent());
        messages.add(gptLatestReply.getMessage());

        // First question
        List<String> attractionNames = getListOfAttractions(target, messages, requestBody);
        print3BlankLines();

        // Second question
        List<String> chapterNames;
        for (String attractionName: attractionNames) {
            messages.remove(messages.size() - 1);
            chapterNames = getListOfChapterNames(target, messages, requestBody, attractionName);

            for (String chapterName : chapterNames) {
                System.out.println(chapterName);

                // third question
                messages.remove(messages.size() - 1);
                String chapterDescription = getChapterDescription(target, messages, requestBody, attractionName,chapterName);
                System.out.println(chapterDescription);
            }

            print3BlankLines();
        }

        if (response != null) {
            response.close();
        }
        if (client != null) {
            client.close();
        }

    }

    private static void print3BlankLines() {
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
    }

    private static List<String> getListOfAttractions(WebTarget target, List<Message> messages, RequestBody requestBody) throws JsonProcessingException {
        List<String> attractionNames = new ArrayList<>();

        Response response = null;
        ResponseBody responseBody;
        ChatReply gptLatestReply;
        messages.add(new Message(Role.USER.name().toLowerCase(), QUESTION_1));
        System.out.println("Dan: " + QUESTION_1);

        boolean noExceptionOccured = false;
        int attempts = 0;
        // i need this because sometimes GPT gives wrong answers like a list or excuses that it can't return JSON format...while other
        // times it works as expected...very weird..
        while (!noExceptionOccured && attempts<3) {
            response = postGPT(requestBody, target);
            responseBody = response.readEntity(ResponseBody.class);
            gptLatestReply = responseBody.getChoices().get(responseBody.getChoices().size() - 1);
            String attractionsReply = gptLatestReply.getMessage().getContent();
            System.out.println("GPT: " + attractionsReply);

            attractionsReply = extractJsonPart(attractionsReply);

            JsonNode valuesNode = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                valuesNode = mapper.readTree(attractionsReply).get("attractions");
                noExceptionOccured = true;
            } catch (JacksonException e) {
                System.out.println(e.getMessage());
                System.out.println(e);
                attempts++;
            }

            for (JsonNode node : valuesNode) {
                attractionNames.add(node.asText());
            }
        }

        if (response != null) {
            response.close();
        }

        return attractionNames;
    }

    private static List<String> getListOfChapterNames(WebTarget target, List<Message> messages, RequestBody requestBody, String attractionName) throws JsonProcessingException {
        List<String> chapterNames = new ArrayList<>();

        Response response = null;
        ResponseBody responseBody;
        ChatReply gptLatestReply;
        messages.add(new Message(Role.USER.name().toLowerCase(), String.format(QUESTION_2,attractionName)));
        System.out.println("Dan: " + String.format(QUESTION_2,attractionName));

        boolean noExceptionOccured = false;
        int attempts = 0;
        // i need this because sometimes GPT gives wrong answers like a list or excuses that it can't return JSON format...while other
        // times it works as expected...very weird..
        while (!noExceptionOccured && attempts<3) {
            response = postGPT(requestBody, target);
            responseBody = response.readEntity(ResponseBody.class);
            gptLatestReply = responseBody.getChoices().get(responseBody.getChoices().size() - 1);
            String attractionsReply = gptLatestReply.getMessage().getContent();
            System.out.println("GPT: " + attractionsReply);

            attractionsReply = extractJsonPart(attractionsReply);

            JsonNode valuesNode = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                valuesNode = mapper.readTree(attractionsReply).get("chapters");
                noExceptionOccured = true;
            } catch (JacksonException e) {
                System.out.println(e.getMessage());
                System.out.println(e);
                attempts++;
            }
            for (JsonNode node : valuesNode) {
                chapterNames.add(node.asText());
            }
        }

        if (response != null) {
            response.close();
        }

        return chapterNames;
    }

    private static String getChapterDescription(WebTarget target, List<Message> messages, RequestBody requestBody, String attractionName, String chapterName) throws JsonProcessingException {
       String chapterDescription = null;

        Response response = null;
        ResponseBody responseBody;
        ChatReply gptLatestReply;
        messages.add(new Message(Role.USER.name().toLowerCase(), String.format(QUESTION_3,attractionName,chapterName)));
        System.out.println("Dan: " + String.format(QUESTION_3,attractionName,chapterName));

        boolean noExceptionOccured = false;
        int attempts = 0;
        // i need this because sometimes GPT gives wrong answers like a list or excuses that it can't return JSON format...while other
        // times it works as expected...very weird..
        while (!noExceptionOccured && attempts<3) {
            response = postGPT(requestBody, target);
            responseBody = response.readEntity(ResponseBody.class);
            gptLatestReply = responseBody.getChoices().get(responseBody.getChoices().size() - 1);
            String attractionsReply = gptLatestReply.getMessage().getContent();
            System.out.println("GPT: " + attractionsReply);

            attractionsReply = extractJsonPart(attractionsReply);

            JsonNode valuesNode = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                valuesNode = mapper.readTree(attractionsReply).get("description");
                noExceptionOccured = true;
            } catch (JacksonException e) {
                System.out.println(e.getMessage());
                System.out.println(e);
                attempts++;
            }

            chapterDescription = valuesNode.asText();
        }

        if (response != null) {
            response.close();
        }

        return chapterDescription;
    }

    private static String extractJsonPart(String attractionsReply) {
        // this is because sometimes this separator is used in replies to show the JSON
        int firstIndex = attractionsReply.indexOf("{");   // idk why it sometimes put the json string there..
        if (firstIndex<0){
            firstIndex = attractionsReply.indexOf("```");
        }
        int lastIndex = attractionsReply.lastIndexOf("}");

        if (firstIndex >0 && lastIndex>0) {
            attractionsReply = attractionsReply.substring(firstIndex, lastIndex+1);
            System.out.println("JSON extracted " + attractionsReply);
        } else {
            System.out.println("JSON delivered " + attractionsReply);
        }
        return attractionsReply;
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
            prop.load(AttractionsGPT.class.getResourceAsStream("/secret.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.get("Authorization").toString();
    }
}
