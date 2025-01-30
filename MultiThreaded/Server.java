import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    public Consumer<Socket> getConsumer()
    {
        return (clientSocket) -> {
            try (PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true))
            {
                toClient.println("Hello from server "+clientSocket.getLocalAddress());
                toClient.close();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        };
    }

    public static void main(String[] args) {
        Server server = new Server();
        int port = 8010;
        try
        {
            ServerSocket socket = new ServerSocket(port);
            socket.setSoTimeout(70000);
            System.out.println("Server is listening on "+port);
            while (true) 
            {
                Socket accepted_socket = socket.accept();
                Thread thread = new Thread(() -> server.getConsumer().accept(accepted_socket));
                thread.start();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
