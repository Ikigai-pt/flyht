package com.flyht.twitter.topics;

import com.flyht.twitter.Constants;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class TopicsProducer implements Runnable{
    final int MAX_TOPICS = 5;

    private final BlockingQueue<String> topicQueue;
    final Scanner scanner = new Scanner(System.in);

    public TopicsProducer(BlockingQueue<String> topicQueue) {
        this.topicQueue = topicQueue;
        applicationExecutionMessage();
    }

    public void applicationExecutionMessage() {
        System.out.println("Enter up to 5 topics");
        System.out.println("### Type exit to quit the application");
        System.out.println("----------------------");
    }

    private boolean validateInput(String topic) {
        return topic != null && topic.length() > 0;
    }

    private boolean shouldAbort(String input) {
        return input.equalsIgnoreCase(Constants.TERMINATE);
    }

    private void stopConsumers() {
        try {
            topicQueue.put(Constants.TERMINATE);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        int counter = 0;
        System.out.println("Enter topic "+(counter+1));
        while(true) {
            String input = scanner.next();
            if (shouldAbort(input)) {
                stopConsumers();
                break;
            }
            if(counter < MAX_TOPICS) {
                if(validateInput(input)) {
                    topicQueue.add(input);
                    counter++;
                } else {
                    System.out.println("Invalid topic name");
                }
                System.out.println("Enter topic "+(counter+1));
            }
            if( counter >= MAX_TOPICS) {
                System.out.println("You have already entered 5 topics, type exit to quite program");
            }
        }
    }
}
