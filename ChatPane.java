package chat;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class ChatPane extends JPanel {

	private static final long serialVersionUID = -2973454267510573242L;

	Socket socket = null;
	BufferedReader bf = null;
	DataOutputStream outputStream = null;
	OutputThread outputThread = null;
	String sender;
	String receiver;
	JTextArea userMessagesArea;

	public void CloseSocketConnection() {
		try {
			socket.close();
			System.out.println("Server is disconnected...");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public ChatPane(Socket s, String sender, String receiver) {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 3, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		JTextArea userMassage = new JTextArea();
		scrollPane.setViewportView(userMassage);

		JButton btnSendMessage = new JButton("Send");
		panel.add(btnSendMessage);

		JButton btnDisconnect = new JButton("Disconnect");
		panel.add(btnDisconnect);

		btnDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {

				CloseSocketConnection();
				btnDisconnect.setText("Socket is disconnected");
				btnDisconnect.setEnabled(false);
				System.exit(ABORT);
			}
		});

		btnSendMessage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (userMessagesArea.getText().trim().length() == 0)
					return;
				try {
					outputStream.writeBytes(userMassage.getText());
					outputStream.write(13);
					outputStream.write(10);
					outputStream.flush();
					userMessagesArea.append("\n" + sender + ": " + userMassage.getText());
					userMassage.setText("");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, BorderLayout.CENTER);

		userMessagesArea = new JTextArea();
		scrollPane_1.setViewportView(userMessagesArea);

		socket = s;
		this.sender = sender;
		this.receiver = receiver;
		try {
			bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = new DataOutputStream(socket.getOutputStream());
			outputThread = new OutputThread(s, userMessagesArea, sender, receiver);
			outputThread.start();
		} catch (Exception e) {

		}
	}

	public JTextArea getuserMessagesArea() {
		return this.userMessagesArea;
	}
}
