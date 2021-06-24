package application.models;
import java.sql.SQLException;
import service.ClientService;
import service.AdminService;
public class TeatruApp {
    public static void main (String[]args) throws SQLException {
        ClientService client = new ClientService();
        AdminService admin =  new AdminService();



    }
}
