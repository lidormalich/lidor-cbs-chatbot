package com.handson.chatbot.controller;

import com.handson.chatbot.service.AmazonService;

import com.handson.chatbot.service.JokeService;
import com.handson.chatbot.service.FreWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    AmazonService amazonService;
    @Autowired
    JokeService joke;
    @Autowired
    FreWebService FREweb;
    @RequestMapping(value = "/jock", method = RequestMethod.GET)
    public ResponseEntity<?> getJock() throws IOException {
        return new ResponseEntity<>(joke.getJock(), HttpStatus.OK);
    }
    @RequestMapping(value = "/amazon", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(amazonService.searchProducts(keyword), HttpStatus.OK);
    }
    @RequestMapping(value = "/fre", method = RequestMethod.GET)
    public ResponseEntity<?> getItem(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(FreWebService.searchProducts(keyword), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = { RequestMethod.POST})
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException {
        HashMap<String, String> params = query.getQueryResult().getParameters();
        String res = "Not found";


        if (params.containsKey("title")) {
             res = FreWebService.searchProducts(params.get("title"));
         }else res = joke.getJock();
        return new ResponseEntity<>(BotResponse.of(res), HttpStatus.OK);
    }
    static class BotQuery {
        QueryResult queryResult;

        public QueryResult getQueryResult() {
            return queryResult;
        }
    }

    static class QueryResult {
        HashMap<String, String> parameters;

        public HashMap<String, String> getParameters() {
            return parameters;
        }
    }

    static class BotResponse {
        String fulfillmentText;
        String source = "BOT";

        public String getFulfillmentText() {
            return fulfillmentText;
        }

        public String getSource() {
            return source;
        }

        public static BotResponse of(String fulfillmentText) {
            BotResponse res = new BotResponse();
            res.fulfillmentText = fulfillmentText;
            return res;
        }
    }
}
