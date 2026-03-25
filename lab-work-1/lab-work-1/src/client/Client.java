package client;

import interfaces.LCMCalculator;
import interfaces.AsyncLCMCalculator;
import interfaces.LCMCallback;
import utils.InputValidator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private final InputValidator validator;
    private final String clientId;
    private LCMCalculator syncService;
    private AsyncLCMCalculator asyncService;
    private LCMCallback callback;

    public Client(String clientId) {
        this.validator = new InputValidator();
        this.clientId = clientId;
    }

    private boolean connect(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);

            syncService = (LCMCalculator) registry.lookup("LCMCalculator");
            asyncService = (AsyncLCMCalculator) registry.lookup("AsyncLCMCalculator");

            callback = new CallbackImpl("Клиент-" + clientId);

            System.out.println("Подключено к серверу " + host + ":" + port);
            return true;

        } catch (Exception e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
            return false;
        }
    }

    private void doSynchronousCall(int a, int b) {
        try {
            System.out.println("\nСинхронный вызов...");
            long start = System.currentTimeMillis();

            long result = syncService.calculateLCM(a, b);

            long time = System.currentTimeMillis() - start;
            System.out.println("Результат: НОК(" + a + ", " + b + ") = " + result);
            System.out.println("Время выполнения: " + time + " мс");

        } catch (Exception e) {
            System.out.println("Ошибка при синхронном вызове: " + e.getMessage());
        }
    }

    private void doAsynchronousCall(int a, int b) {
        try {
            System.out.println("\nАсинхронный вызов (результат придет через callback)...");
            long start = System.currentTimeMillis();

            asyncService.calculateLCMAsync(a, b, callback);

            long time = System.currentTimeMillis() - start;
            System.out.println("Время отправки: " + time + " мс");
            System.out.println("Клиент продолжает работу...");

            for (int i = 1; i <= 3; i++) {
                Thread.sleep(500);
                System.out.println("Клиент работает... (" + i + "/3)");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при асинхронном вызове: " + e.getMessage());
        }
    }

    private void showMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("КЛИЕНТ #" + clientId);
        System.out.println("=".repeat(50));
        System.out.println("Доступные команды:");
        System.out.println("  calc <число1> <число2>  - синхронный расчет НОК");
        System.out.println("  async <число1> <число2> - асинхронный расчет НОК");
        System.out.println("  меню                    - показать это меню");
        System.out.println("  выход                   - завершить работу");
        System.out.println("=".repeat(50));
    }

    public void run(String host, int port) {
        System.out.println("\nЗАПУСК КЛИЕНТА #" + clientId);
        System.out.println("=".repeat(50));

        if (!connect(host, port)) {
            System.out.println("Не удалось подключиться к серверу. Проверьте, запущен ли сервер.");
            return;
        }

        showMenu();

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            System.out.print("\nВведите команду: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("выход") || input.equals("exit")) {
                System.out.println("До свидания!");
                break;
            }

            if (input.equals("меню") || input.equals("menu") || input.equals("help")) {
                showMenu();
                continue;
            }

            if (input.startsWith("calc ")) {
                String[] parts = input.split("\\s+");
                if (parts.length != 3) {
                    System.out.println("Неправильный формат. Используйте: calc <число1> <число2>");
                    continue;
                }

                try {
                    int a = Integer.parseInt(parts[1]);
                    int b = Integer.parseInt(parts[2]);
                    doSynchronousCall(a, b);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите целые числа!");
                }

            } else if (input.startsWith("async ")) {
                String[] parts = input.split("\\s+");
                if (parts.length != 3) {
                    System.out.println("Неправильный формат. Используйте: async <число1> <число2>");
                    continue;
                }

                try {
                    int a = Integer.parseInt(parts[1]);
                    int b = Integer.parseInt(parts[2]);
                    doAsynchronousCall(a, b);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите целые числа!");
                }

            } else if (!input.isEmpty()) {
                System.out.println("Неизвестная команда. Введите 'меню' для списка команд.");
            }
        }

        scanner.close();
        validator.close();
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1099;
        String clientId = "1";

        if (args.length > 0) {
            host = args[0];
        }
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Порт должен быть числом. Использую порт 1099");
            }
        }
        if (args.length > 2) {
            clientId = args[2];
        }

        Client client = new Client(clientId);
        client.run(host, port);
    }
}