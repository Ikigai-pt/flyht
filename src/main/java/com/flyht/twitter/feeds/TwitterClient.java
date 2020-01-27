package com.flyht.twitter.feeds;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterClient {
    final ConfigurationBuilder config = new ConfigurationBuilder();
    TwitterFactory twitterFactory;

    void setUp(){
        config.setDebugEnabled(true)
                .setOAuthConsumerKey("X7V87RuH9a4nsTYt57BLUEvT6")
                .setOAuthConsumerSecret("enVXrcWXivtV6KPCGPXDgqeGhWTeaIbgoYEI3Iyp3umfF9tQdR")
                .setOAuthAccessToken("64204167-jLiwahiN9lugbwb8RbhZpkhbshEWuGkYpVOEUrwgd")
                .setOAuthAccessTokenSecret("c3BR6FG33hkWCvuPECSVeOfm3DDfn9FtKPP5eDQ1tbhwc");
        twitterFactory =  new TwitterFactory(config.build());
    }

    public TwitterClient(){
        setUp();
    }

    public Twitter getTwitterInstance() {
        return twitterFactory.getInstance();
    }

}
