package com.flyht.twitter.feeds;

import java.util.List;

public class Tweet {
    List<String> content;
    String topic;

    public Tweet(String topic, List<String> content) {
        this.content = content;
        this.topic = topic;
    }

    public List<String> getContent() {
        return content;
    }

    public String getTopic() {
        return topic;
    }
}
