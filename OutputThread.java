package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JTextArea;

public class OutputThread extends Thread {

	Socket socket; // socket is used to communication
	JTextArea message; // message is the communicated chat message
	BufferedReader bf; // input buffer of the socket
	String sender; // this is one side of the communication
	String receiver; // receive the other side of the communication

	public OutputThread(Socket socket, JTextArea message, String sender, String receiver) {
		super();
		this.socket = socket;
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
		
		try {
			bf  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true)
			try {
				if (socket != null) {
					String msg = "";
					if ((msg = bf.readLine()) != null && msg.length() > 0) {
						message.append("\n" + receiver + ": " + msg);
					}
					sleep(1000);
				}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
