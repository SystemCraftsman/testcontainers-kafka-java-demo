package com.systemcraftsman.demo;

public class App {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            NotificationProducer.produce();
            Thread.sleep(5000);
        }
    }
}
