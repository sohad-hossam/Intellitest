package z;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.util.DataCheck;
import unisa.gps.etour.util.ErrorMessages;

public class Authentication extends UnicastRemoteObject implements IAuthentication {

    private static final long serialVersionUID = 0L;

    public Authentication() throws RemoteException {
        super();
    }

    // Objects to manipulate tourist data
    private DBTourist touristDB = new DBTourist();
    private BeanTourist bTourist;

    public int login(String pUsername, String pPassword, byte pUserType) throws RemoteException {
        // Check if the string username and password
        if (DataCheck.checkString(pUsername, true, true, "_-", null, 6, 12)
                && DataCheck.checkString(pPassword, true, true, "_-", null, 5, 12)) {
            try {
                switch (pUserType) {
                    // If the type is Tourist
                    case IAuthentication.VISITOR:
                        // Invoke the method to obtain the Tourist Bean
                        // Given the username
                        bTourist = touristDB.getTourist(pUsername);
                        // Check that the Bean is not null and
                        // Passwords match
                        if (bTourist != null && bTourist.getPassword().equals(pPassword))
                            return bTourist.getId();
                        // If the type is not Tourist
                    case IAuthentication.RESTAURANT:
                        // Not implemented for the restaurant
                        // Refreshment
                        return -1;
                    // If it does not match any known type
                    default:
                        return -1;
                }
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessages.DBMS_ERROR);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
            }
        }
        // If the data are incorrect returns -1
        return -1;
    }
}
