package com.flyht.twitter.feeds;

import com.flyht.twitter.exception.TwitterServiceException;
import twitter4j.*;

import java.util.List;
import java.util.stream.Collectors;

public class TwitterService {
    private final TwitterClient twitterClient = new TwitterClient();
    final Twitter twitter = twitterClient.getTwitterInstance();

    public QueryResult getQueryResult(String topic) {
        try {
            Query query = new Query(topic);
            return twitter.search(query);
        } catch (TwitterException ex) {
            System.out.println(ex);
            throw new TwitterServiceException("Twitter fetch failed "+ex);
        }
    }

    public List<String> fetchTopic(QueryResult result) {
        return result.getTweets().stream().map(Status::getText).collect(Collectors.toList());
    }
}
