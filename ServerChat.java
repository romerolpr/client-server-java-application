package chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerChat extends JFrame {

	
	private static final long serialVersionUID = -517952492485627716L;
	private JPanel contentPane;
	private JTextField textAdmin;
	private JTextField textIp;
	private JTextField textPort;

	Socket mngSocket = null;
	String AdminName = "";
	String mngIp = "";
	int mngPort = 0;
	BufferedReader bf = null;
	DataOutputStream os = null;
	OutputThread t = null;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerChat frame = new ServerChat();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public ServerChat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 610, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Admin & Server Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 7, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Admin");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);
		
		textAdmin = new JTextField();
		textAdmin.setText("UNIP");
		panel.add(textAdmin);
		textAdmin.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Mng Ip");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel_1);
		
		textIp = new JTextField();
		textIp.setText("localhost");
		panel.add(textIp);
		textIp.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Port:");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel_2);
		
		textPort = new JTextField();
		textPort.setText("43789");
		panel.add(textPort);
		textPort.setColumns(10);
		
		JFrame thisFrame = this;
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mngIp = textIp.getText();
				mngPort = Integer.parseInt(textPort.getText());
				AdminName = textAdmin.getText();
				try {
					mngSocket = new Socket(mngIp, mngPort);
					if (mngSocket != null) {
						ChatPane p = new ChatPane(mngSocket, AdminName, "Client");
						thisFrame.getContentPane().add(p);
						p.getuserMessagesArea().append(AdminName + " is connected...");
						p.updateUI();
						
						bf = new BufferedReader(new InputStreamReader(mngSocket.getInputStream()));
						os = new DataOutputStream(mngSocket.getOutputStream());
						os.writeBytes(AdminName);
						os.write(13);	
						os.write(10);	
						os.flush();
					}
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(btnConnect);
	}

}
