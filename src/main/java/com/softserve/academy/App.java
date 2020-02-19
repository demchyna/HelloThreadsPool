package com.softserve.academy;

import javax.naming.NamingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

    static private AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws IOException, InterruptedException, NamingException {

        List<String> data = Files.readAllLines(Paths.get("data1.txt"));

        ExecutorService executorService = Executors.newFixedThreadPool(data.size());

        List<Future<String>> futures = new ArrayList<>();

        data.forEach((line) -> {
            Future<String> future = executorService.submit(() -> {
                Thread.sleep(2000);
                return line.toUpperCase();
            });
            futures.add(future);
        });

        executorService.shutdown();

        futures.forEach((item) -> {
            try {
                data.set(counter.getAndAdd(1), item.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        data.forEach(System.out::println);

        Files.write(Paths.get("data2.txt"), data);

//        List<Callable<String>> callables = new ArrayList<>();
//        data.forEach((line) -> {
//            callables.add(() -> {
//                Thread.sleep(2000);
//                return line.toUpperCase();
//            });
//        });
//
//        List<Future<String>> futures = executorService.invokeAll(callables);
//        futures.forEach((item) -> {
//            try {
//                data.set(counter.getAndAdd(1), item.get());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        });
//        executorService.shutdown();
//        data.forEach(System.out::println);
//
//        Files.write(Paths.get("data2.txt"), data);

    }
}