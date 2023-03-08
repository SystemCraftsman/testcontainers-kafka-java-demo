package com.systemcraftsman.demo;

public class App {
    public static void main(String[] args) throws InterruptedException {
        NotificationProducer producer = new NotificationProducer();
        while (true) {
            producer.produce();
            Thread.sleep(5000);
        }
    }
}

