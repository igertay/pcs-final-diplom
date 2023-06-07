import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Request {

    public static void main(String[] args) {

        try (Socket socket = new Socket(Main.HOST, Main.PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println(reader.readLine());
            writer.println(new Scanner(System.in)
                    .nextLine());
            String result;
            while ((result = reader.readLine()) != null) {
                System.out.println(result);
            }

        } catch (IOException e) {
            System.out.println("Клиент не запускается");
            e.printStackTrace();
        }
    }
}