package com.tinkoff.androidcourse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorkerGenerator {
    private static List<String> maleNames = new ArrayList<>(Arrays.asList("John", "Bill", "Bob", "Oliver", "Jack", "Harry", "George", "William", "Henry"));
    private static List<String> femaleNames = new ArrayList<>(Arrays.asList("Anna", "Emma", "Sophie", "Jessica", "Scarlett", "Molly", "Lucy", "Megan"));
    private static List<String> surnames = new ArrayList<>(Arrays.asList("Green", "Smith", "Taylor", "Brown", "Wilson", "Walker", "White", "Jackson", "Wood"));
    private static List<Integer> femalePhoto = new ArrayList<>(Arrays.asList(R.drawable.ic_female_black, R.drawable.ic_female_green, R.drawable.ic_female_red, R.drawable.ic_female_magnetta));
    private static List<Integer> malePhoto = new ArrayList<>(Arrays.asList(R.drawable.ic_male_black, R.drawable.ic_male_green, R.drawable.ic_male_red, R.drawable.ic_male_magnetta));
    private static List<String> positions = new ArrayList<>(Arrays.asList("Android programmer", "IOS programmer", "Web programmer", "Designer"));

    public static Worker generateWorker() {
        Worker worker = new Worker();
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(2);
        if (index == 0) {
            index = randomGenerator.nextInt(maleNames.size());
            String randomName = maleNames.get(index);
            index = randomGenerator.nextInt(surnames.size());
            String randomSurname = surnames.get(index);
            worker.setName(randomName + " " + randomSurname);
            index = randomGenerator.nextInt(malePhoto.size());
            Integer randomPhoto = malePhoto.get(index);
            worker.setPhoto(randomPhoto);
        } else {
            index = randomGenerator.nextInt(femaleNames.size());
            String randomName = femaleNames.get(index);
            index = randomGenerator.nextInt(surnames.size());
            String randomSurname = surnames.get(index);
            worker.setName(randomName + " " + randomSurname);
            index = randomGenerator.nextInt(femalePhoto.size());
            Integer randomPhoto = femalePhoto.get(index);
            worker.setPhoto(randomPhoto);
        }
        worker.setAge(Integer.toString(20 + randomGenerator.nextInt(10)));
        index = randomGenerator.nextInt(positions.size());
        worker.setPosition(positions.get(index));
        return worker;
    }

    public static List<Worker> generateWorkers(int workersCount) {
        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < workersCount; i++) {
            workers.add(generateWorker());
        }
        return workers;
    }

}
