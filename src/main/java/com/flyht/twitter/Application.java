package com.flyht.twitter;

import com.flyht.twitter.feeds.Tweet;
import com.flyht.twitter.feeds.TweetPersistenceConsumer;
import com.flyht.twitter.topics.TopicsConsumer;
import com.flyht.twitter.topics.TopicsProducer;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Application {
    final static int MAX_TOPICS = 5;
    final static int MAX_TOPIC_CONSUMERS = 3;
    final static int MAX_TWEET_CONSUMERS = 3;

    public static void runWithThreadPool() {
        // This implementation will scale if topic is more than 5
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
        BlockingQueue<String> topicQueue = new LinkedBlockingQueue<String>();
        BlockingQueue<Tweet> tweetsQueue = new LinkedBlockingQueue<>();

        Thread topicProducer = new Thread(new TopicsProducer(topicQueue));
        executor.execute(topicProducer);

        IntStream.range(0, MAX_TOPIC_CONSUMERS)
                .mapToObj(i -> new Thread(new TopicsConsumer(topicQueue, tweetsQueue)))
                .forEach(executor::execute);

        IntStream.range(0, MAX_TWEET_CONSUMERS)
                .mapToObj(i -> new Thread(new TweetPersistenceConsumer(tweetsQueue)))
                .forEach(executor::execute);

        executor.shutdown();
    }

    public static void main(String [] args) {
        runWithThreadPool();
    }
}
