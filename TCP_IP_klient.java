
package TCP_IP_klient;


import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class TCP_IP_klient {
    
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private JTextArea txtArea;

    public static void main(String[] args) {
        TCP_IP_klient client = new TCP_IP_klient();
        client.createGUI();
        
        client.start();
    }
    
    public void start()
    {
        Thread mainThread = new Thread(()->listen());
        mainThread.start();
    }
    void createGUI()
    {
        JFrame frame = new JFrame("Klient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(1000,300);
        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        
        
        txtArea = new JTextArea(10,20);
        JScrollPane scroll = new JScrollPane(txtArea);
        panelTop.add(scroll);  
        
        JTextField txtMessage = new JTextField(20);
        JButton btnSend = new JButton("Wyślij");
        btnSend.addActionListener(ActionEvent->
        {
            try
            {
                writer.println(txtMessage.getText());
                txtMessage.setText("");            
                writer.flush();
                txtMessage.requestFocus();
            }
            catch(java.lang.NullPointerException e)
            {
                JOptionPane.showMessageDialog(panelTop, "Brak połączenia");
            }
            
        });
        panelBottom.add(txtMessage);
        panelBottom.add(btnSend);
        
        JButton btnIP = new JButton("Połącz");        
        JLabel lblIP = new JLabel("IP: ");
        JPanel panel1 = new JPanel();
        JTextField txtIP = new JTextField("127.0.0.1",10);
        
        btnIP.addActionListener((ActionEvent)->
        {
            connect(txtIP.getText());
            start();
        });
        panel1.add(lblIP);
        panel1.add(txtIP);
        panel1.add(btnIP);
        
        frame.add(panel1);
        frame.add(panelTop);
        frame.add(panelBottom);
        
        frame.setVisible(true);        
        frame.pack();        
        txtMessage.requestFocus();
    }
    
    public void connect(String addressIP)
    {
        try 
        {
            socket = new Socket(addressIP, 6000);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
            txtArea.append(socket.getInetAddress()+"\n");
        } 
        catch (IOException ex) 
        {
            JOptionPane.showMessageDialog(null, "Brak połączenia");
        }        
    }
    public void listen() 
    {
        if(reader == null) return;
        try {
            String message;
            while((message=reader.readLine())!=null)
            {
                txtArea.append(message+'\n');
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Utracono połączenie");   
            System.exit(0);
        }
    }
}
