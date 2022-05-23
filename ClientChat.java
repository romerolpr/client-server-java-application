package chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;

public class ClientChat <ActionEvent> extends JFrame implements Runnable {

	private static final long serialVersionUID = 2709569238621511504L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	ServerSocket srvSocket = null;
	BufferedReader br = null;
	Thread t;

	@SuppressWarnings("unused")
	private JTextField txtServerPort;
	private JTextField txtServer;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("rawtypes")
			public void run() {
				try {
					ClientChat frame = new ClientChat();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public ClientChat() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 610, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		JLabel lblNewLabel = new JLabel("Configure Port:");
		panel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtServer = new JTextField();
		txtServer.setText("43789");
		panel.add(txtServer);
		txtServer.setColumns(10);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);


		this.setSize(600, 300);
		int serverPort = Integer.parseInt(txtServer.getText());
		try {
			srvSocket = new ServerSocket(serverPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
		t = new Thread(this);
		t.start();
	}

	public void run() {
		while (true) {
			try {
				Socket aSocket = srvSocket.accept();
				if (aSocket != null) {
					br = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
					String s = br.readLine();
					// UserName lether's position
					int pos = s.indexOf("");
					String srvName = s.substring(pos);
					ChatPane p = new ChatPane(aSocket, "Client", srvName);
					tabbedPane.add(srvName, p);
					p.updateUI();
				}
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
