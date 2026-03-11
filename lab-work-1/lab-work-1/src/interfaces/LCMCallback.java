package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LCMCallback extends Remote {
    void onSuccess(long result) throws RemoteException;
    void onError(String errorMessage) throws RemoteException;
}