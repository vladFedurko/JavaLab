package Lab_1;

import java.lang.Class;
import java.lang.reflect.Constructor;
import java.util.*;

public class MainLab1 {
    public static void toStart(String[] args) {
        boolean sortFlag = false;
        boolean caloriesFlag = false;
        Food[] breakfast = new Food[20];
        int i = -1;

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (arg.equals("-sort"))
                    sortFlag = true;
                if (arg.equals("-calories"))
                    caloriesFlag = true;
                continue;
            }
            String[] parts = arg.split("/");
            Class myClass = null;
            try {
                myClass = Class.forName("Lab_1." + parts[0]);
            } catch (ClassNotFoundException exc) {
                exc.getCause();
                System.out.println("Всё плохо! Скорее всего вы неправильно ввели название класса!");
                continue;
            }
            Constructor constructor = null;
            try {
                switch (parts.length) {
                    case 1:
                        constructor = myClass.getConstructor();
                        breakfast[++i] = (Food) constructor.newInstance();
                        break;
                    case 2:
                        constructor = myClass.getConstructor(String.class);
                        breakfast[++i] = (Food) constructor.newInstance(parts[1]);
                        break;
                    case 3:
                        constructor = myClass.getConstructor(String.class, String.class);
                        breakfast[++i] = (Food) constructor.newInstance(parts[1], parts[2]);
                        break;
                    default:
                        System.out.println("Ups");
                }
            } catch (NoSuchMethodException exc) {
                exc.getCause();
                System.out.println("Всё плохо! Скорее всего вы недопустимое количество аргументов класса!");
            } catch (Exception exc) {
                exc.getCause();
            }
        }
        if (sortFlag) {
            Arrays.sort(breakfast, new Comparator() {
                public int compare(Object f1, Object f2) {
                    if (f1 == null) return 1;
                    if (f2 == null) return -1;
                    int a = ((Food) f1).calculateCalories();
                    int b = ((Food) f2).calculateCalories();
                    if (a == b) return 0;
                    if (a > b) return -1;
                    return 1;
                }
            });
        }
        for (Food item: breakfast) {
            if(item != null)
                item.consume();
            else
                break;
        }
        if (caloriesFlag){
            long sum = 0;
            for (Food item: breakfast) {
                if(item != null)
                    sum += item.calculateCalories();
                else
                    break;
            }
            System.out.println("Калорийность завтрака: " + sum);
        }
        System.out.println("Всего хорошего!");
    }
}