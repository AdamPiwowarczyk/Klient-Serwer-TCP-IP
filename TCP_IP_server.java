/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP_IP_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;



public class TCP_IP_server {
    
    private Socket socket;
    private JTextArea txtArea;
    private ArrayList<PrintWriter> writersList = new ArrayList<>();
    ServerSocket srvSocket;

    
    

    public static void main(String[] args) {
        TCP_IP_server server = new TCP_IP_server();
        server.createGUI();
        server.connect();
    }

    public void createGUI()
    {
        JFrame frame = new JFrame("Serwer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(600,300);
        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(panelTop);
        frame.add(panelBottom);
        
        txtArea = new JTextArea(10,20);
        JScrollPane scroll = new JScrollPane(txtArea);
        panelTop.add(scroll);  
        
        JTextField txtMessage = new JTextField(20);
        JButton btnSend = new JButton("Wyślij");
        btnSend.addActionListener(e->
        {
            try
            {
                for(PrintWriter writer : writersList)
                {
                    writer.println(txtMessage.getText());
                    //System.out.println(txtMessage.getText());//?                    
                    writer.flush();                    
                }    
                txtMessage.setText("");
                txtMessage.requestFocus();
            }
            catch(java.lang.NullPointerException ex)
            {
                JOptionPane.showMessageDialog(null, "Brak połączenia");
            }
            
        });
        
        
        panelBottom.add(txtMessage);
        panelBottom.add(btnSend);
        
        frame.setVisible(true);
        txtMessage.requestFocus();
        frame.pack();          
    }
    
    public void connect()
    {           
        try {
            srvSocket = new ServerSocket(6000);
        } catch (IOException ex) {
            Logger.getLogger(TCP_IP_server.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true)
        {            
            try
            {
                socket = srvSocket.accept();

                writersList.add(new PrintWriter(socket.getOutputStream()));
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                new Thread(new ClientThread(reader)).start();

                txtArea.append("połączono\n");
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(TCP_IP_server.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
                
    }
    public void read(BufferedReader reader)
    {
        try {
            String message;
            while((message = reader.readLine())!=null)
                {
                    txtArea.append(message+'\n');
                }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Utracono połączenie");
        }      
    }
    public class ClientThread implements Runnable
    {
        private BufferedReader reader;

        public ClientThread(BufferedReader reader)
        {
            this.reader = reader;
        }
        
        @Override
        public void run()
        {
            read(reader);                                   
        }
                        
    }       
            
            
            
            
         
    
    
}
/*
klasa
{
    ArrayList lista;
    wątek main()
    {
        wątek_1 dopisujący do ArrayList;

        wątek_2 czytający arrayList; - nie działa/ działa dla arrayList sprzed startu tego wątka (czyli pustej)
    }

}
*/