package com.flyht.twitter.feeds;

import com.flyht.twitter.Constants;

import java.util.concurrent.BlockingQueue;
// Read a batch of tweets from tweetQueue and persist the to file
// Tweet object contains topic & a batch of tweets
public class TweetPersistenceConsumer implements  Runnable{
    private final BlockingQueue<Tweet> tweetQueue;
    private final FileWriter fileWriter = new FileWriter();

    public TweetPersistenceConsumer(BlockingQueue<Tweet> tweetQueue) {
        this.tweetQueue = tweetQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tweet tweet = tweetQueue.take();
                if(tweet.topic.equalsIgnoreCase(Constants.TERMINATE)) {
                    System.out.println("Exit Twitter consumer :"+Thread.currentThread().getName());
                    break;
                }
                fileWriter.writeTweetsToFile(tweet.topic, String.join("\n", tweet.content));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
