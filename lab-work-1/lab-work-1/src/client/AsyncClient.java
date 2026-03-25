package client;

import interfaces.AsyncLCMCalculator;
import interfaces.LCMCallback;
import utils.InputValidator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AsyncClient {

    public static void main(String[] args) {
        InputValidator validator = new InputValidator();

        try {
            System.out.println("\nАСИНХРОННЫЙ КЛИЕНТ");
            System.out.println("=".repeat(50));

            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AsyncLCMCalculator service = (AsyncLCMCalculator) registry.lookup("AsyncLCMCalculator");

            LCMCallback callback = new CallbackImpl("AsyncDemo");

            System.out.println("Подключено к серверу\n");

            while (true) {
                System.out.println("-".repeat(40));
                int a = validator.readInt("Введите первое число: ");
                int b = validator.readInt("Введите второе число: ");

                System.out.println("\nОтправка асинхронного запроса...");
                service.calculateLCMAsync(a, b, callback);

                System.out.println("Запрос отправлен. Ожидание callback...\n");

                for (int i = 1; i <= 5; i++) {
                    Thread.sleep(400);
                    System.out.println("   Работаем... " + i + "/5");
                }

                if (!validator.askContinue()) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            validator.close();
        }

        System.out.println("Программа завершена");
    }
}