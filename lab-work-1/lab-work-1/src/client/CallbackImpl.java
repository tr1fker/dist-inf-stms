package client;

import interfaces.LCMCallback;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallbackImpl extends UnicastRemoteObject implements LCMCallback {

    private final String clientName;

    protected CallbackImpl(String clientName) throws RemoteException {
        super();
        this.clientName = clientName;
    }

    @Override
    public void onSuccess(long result) throws RemoteException {
        System.out.println("\n[CALLBACK] Клиент '" + clientName + "' получил результат: " + result);
        System.out.print("Введите следующую команду (или 'меню' для возврата): ");
    }

    @Override
    public void onError(String errorMessage) throws RemoteException {
        System.out.println("\n[CALLBACK] Клиент '" + clientName + "' получил ошибку: " + errorMessage);
        System.out.print("Введите следующую команду (или 'меню' для возврата): ");
    }
}