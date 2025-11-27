package ua.opnu;

import java.util.*;
import java.util.function.*;

public class Lab7 {

    // ===== Допоміжні константи/утиліти =====
    private static final String[] UA_DIGITS = {
            "нуль","один","два","три","чотири","п'ять","шість","сім","вісім","дев'ять"
    };

    // Маленькі утиліти друку (зроблено public для використання у Main)
    public static void printArray(int[] a) { System.out.println(Arrays.toString(a)); }

    public static void printStudents(Student[] arr) {
        for (Student s : arr) {
            System.out.printf("%s %s (%s), avg=%.1f%n",
                    s.getSurname(), s.getName(), s.getGroup(), s.avg());
        }
    }

    // Одноумовна версія фільтра для int[] (утиліта для Task 3)
    public static int[] filter(int[] data, Predicate<Integer> p) {
        int cnt = 0;
        for (int v : data) if (p.test(v)) cnt++;
        int[] res = new int[cnt];
        int i = 0;
        for (int v : data) if (p.test(v)) res[i++] = v;
        return res;
    }


    public static class Student {
        private String fullName;   // формат: "Прізвище Ім'я"
        private String group;
        private int[] marks;       // 0..100

        public Student(String fullName, String group, int... marks) {
            this.fullName = fullName == null ? "" : fullName.trim();
            this.group = group == null ? "" : group.trim();
            this.marks = marks == null ? new int[0] : marks.clone();
        }

        public String getFullName() { return fullName; }
        public String getGroup() { return group; }
        public int[] getMarks() { return marks.clone(); }

        public void setFullName(String fullName) { this.fullName = fullName == null ? "" : fullName.trim(); }
        public void setGroup(String group) { this.group = group == null ? "" : group.trim(); }
        public void setMarks(int[] marks) { this.marks = marks == null ? new int[0] : marks.clone(); }

        /** Середній бал. */
        public double avg() {
            if (marks.length == 0) return 0.0;
            int sum = 0;
            for (int m : marks) sum += m;
            return 1.0 * sum / marks.length;
        }

        /** true, якщо є оцінка < 60 (борг). */
        public boolean hasDebt() {
            for (int m : marks) if (m < 60) return true;
            return false;
        }

        /** Перше слово з fullName. */
        public String getSurname() {
            String[] parts = fullName.split("\\s+");
            return parts.length > 0 ? parts[0] : "";
        }

        /** Друге слово з fullName (або усе після першого). */
        public String getName() {
            String[] parts = fullName.split("\\s+");
            if (parts.length <= 1) return "";
            if (parts.length == 2) return parts[1];
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                if (i > 1) sb.append(' ');
                sb.append(parts[i]);
            }
            return sb.toString();
        }
    }

    // ====== Реалізація завдань ======

    // Task 1 — Predicate: просте число
    // Пояснення: метод перевіряє простоту числа пробним діленням до sqrt(n).
    public static final Predicate<Integer> IS_PRIME = Lab7::isPrime;

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        int lim = (int)Math.sqrt(n);
        for (int d = 3; d <= lim; d += 2) {
            if (n % d == 0) return false;
        }
        return true;
    }

    // Task 2 — Фільтрація студентів Predicate<Student>
    // Пояснення: повертаємо новий масив студентів, що задовольняють предикат.
    public static Student[] filterStudents(Student[] arr, Predicate<Student> p) {
        int cnt = 0;
        for (Student s : arr) if (p.test(s)) cnt++;
        Student[] res = new Student[cnt];
        int i = 0;
        for (Student s : arr) if (p.test(s)) res[i++] = s;
        return res;
    }

    // Task 3 — Подвійний фільтр (AND двох предикатів) для int[]
    // Пояснення: пропускаємо елемент лише якщо обидва предикати p1 і p2 істинні.
    public static int[] filterAnd(int[] data, Predicate<Integer> p1, Predicate<Integer> p2) {
        return filter(data, p1.and(p2));
    }

    // Task 4 — Consumer<Student> + forEach
    // Пояснення: застосовуємо дію до кожного студента (аналог простого foreach).
    public static void forEach(Student[] arr, Consumer<Student> action) {
        for (Student s : arr) action.accept(s);
    }

    // Task 5 — doIf: Predicate + Consumer для int[]
    // Пояснення: виконуємо дію лише для елементів, що задовольняють умову.
    public static void doIf(int[] data, Predicate<Integer> cond, Consumer<Integer> action) {
        for (int v : data) if (cond.test(v)) action.accept(v);
    }

    // Task 6 — Function<Integer,Integer> (2^n) для int[]
    // Пояснення: застосовуємо функцію до кожного елемента та повертаємо новий масив.
    public static int[] processIntArray(int[] arr, Function<Integer, Integer> f) {
        int[] out = new int[arr.length];
        for (int i = 0; i < arr.length; i++) out[i] = f.apply(arr[i]);
        return out;
    }

    // Task 7 — stringify() + словник числівників
    // Пояснення: відображаємо елементи масиву у рядки за допомогою словника-функції.
    public static String[] stringify(int[] arr, Function<Integer, String> dict) {
        String[] out = new String[arr.length];
        for (int i = 0; i < arr.length; i++) out[i] = dict.apply(arr[i]);
        return out;
    }

    /** Повертає словникову функцію 0..9 -> українські назви. */
    public static Function<Integer,String> digitsUa() {
        return n -> (n >= 0 && n <= 9) ? UA_DIGITS[n] : "?";
    }

    /** Створює масив [from..toExclusive-1]. */
    public static int[] range(int from, int toExclusive) {
        int n = Math.max(0, toExclusive - from);
        int[] a = new int[n];
        for (int i = 0, v = from; i < n; i++, v++) a[i] = v;
        return a;
    }
}
