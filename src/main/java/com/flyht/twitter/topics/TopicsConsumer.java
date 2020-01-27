package com.flyht.twitter.topics;

import com.flyht.twitter.Constants;
import com.flyht.twitter.exception.ApplicationException;
import com.flyht.twitter.feeds.Tweet;
import com.flyht.twitter.feeds.TwitterService;
import twitter4j.Query;
import twitter4j.QueryResult;

import java.util.List;
import java.util.concurrent.BlockingQueue;

// Topic consumer reads the inputs entered in command line
// It also acts as a producer of tweets and pushes them into tweetsQueue
// If it receive TERMINATE command then circuit break the while look and
// also issue TERMINATE to its consumers via tweetsQueue

public class TopicsConsumer implements Runnable {

    private final BlockingQueue<String> topicsQueue;
    private final BlockingQueue<Tweet> tweetsQueue;

    private final TwitterService twitterService = new TwitterService();

    public TopicsConsumer(BlockingQueue<String> topicsQueue, BlockingQueue<Tweet> tweetsQueue) {
       this.topicsQueue = topicsQueue;
       this.tweetsQueue = tweetsQueue;
    }

    @Override
    public void run() {
        try {
            String receivedTopic = null;
            Query query = null;
            do{
                // Check if there are topics from topic producer
                String topic = topicsQueue.peek();
                if( topic != null) {
                    // Check if the input is an abort command
                    if(topic.equalsIgnoreCase(Constants.TERMINATE)) {
                        System.out.println("Exit Topic :"+Thread.currentThread().getName());
                        // Send ABORT to twitterPersistence service to abort file write
                        tweetsQueue.add(new Tweet( Constants.TERMINATE, null));
                        break;
                    } else {
                        // pop topic from queue
                        receivedTopic = topicsQueue.take();
                    }
                }
                // if topic is not null, then fetch tweets from twitter API
                if(receivedTopic!=null){
                    QueryResult result = twitterService.getQueryResult(receivedTopic);
                    List<String > content = twitterService.fetchTopic(result);
                    tweetsQueue.add(new Tweet(receivedTopic,content ));

                    // Added Thread sleep to avoid Twitter API rate limit
                    // Also use Twitter Rate limit Exception to dynamically put the
                    // thread to sleep and retry
                    Thread.sleep(5000);
                    // loop through the tweets via results pagination using nextQuery
                    query = result.nextQuery();
                }
                // if all tweets are read then query will be null & terminate the process
            } while ( query != null || receivedTopic == null);

        } catch (RuntimeException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApplicationException("Execution Aborted "+ex);
        }
    }

}
