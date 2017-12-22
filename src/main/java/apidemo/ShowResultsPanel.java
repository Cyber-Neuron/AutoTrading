
package apidemo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
/**
 *
 * @author jyang
 */
public class ShowResultsPanel extends JPanel implements ActionListener{
    
    private String Panel_Name;
    
    private JButton openButton;
    private JTextArea log;
    private JFileChooser fChoose;
    private JButton loadButton;
    private File selectedFile;
    private ArrayList<String> symbols;
    
    public ShowResultsPanel(String panelname){
        this.Panel_Name = panelname;
        symbols = new ArrayList<>();
        log = new JTextArea(5, 20);
     
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        
        fChoose = new JFileChooser();
        openButton = new JButton("Show results");
        //loadButton = new JButton("Request data to symbols");
        openButton.addActionListener(this);
        //loadButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        //buttonPanel.add(loadButton);
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS) );
        //add(buttonPanel, BorderLayout.NORTH);
        //add(logScrollPane, BorderLayout.SOUTH);
        add(buttonPanel);
        add(logScrollPane);
    }
    
    public void actionPerformed(ActionEvent e){
         if(e.getSource() == openButton){
              int returnVal = fChoose.showOpenDialog(ShowResultsPanel.this);
              
              if(returnVal == JFileChooser.APPROVE_OPTION){
                
                 File file = fChoose.getSelectedFile();
                 selectedFile = file;
                 log.append("Opening: " + file.getName() + "." + "\n");
                 log.append(file.getAbsolutePath() + "\n");
                 
                 if(selectedFile != null && selectedFile.exists()){
                     try{
                        FileReader fr = new FileReader(selectedFile.getAbsolutePath());
                        BufferedReader br = new BufferedReader(fr);
                        String content = "";
                        if (symbols == null)
                            this.symbols = new ArrayList<String>();
                        while((content = br.readLine()) != null){
                            
                             this.symbols.add(content.trim().toUpperCase());
                             log.append(content.trim().toUpperCase() + "\n");
                        }
                        br.close();
                        fr.close();
                     }catch(Exception ex){
                         ex.printStackTrace();
                     }
                 }
                 
             }else{
                 log.append("Open command cancelled by user." + "\n");
              }
              log.setCaretPosition(log.getDocument().getLength());
         }
//         if(e.getSource() == loadButton){
//              
//             if(selectedFile != null && selectedFile.exists()){
//                 try{
//                     FileReader fr = new FileReader(selectedFile.getAbsolutePath());
//                     BufferedReader br = new BufferedReader(fr);
//                     String content = "";
//                     ArrayList<String> symbols = new ArrayList<String>();
//                     while((content = br.readLine()) != null){
//                         symbols.add(content.trim().toUpperCase());
//                     }
//                     br.close();
//                     fr.close();
//                     this.symbols = symbols;
//                 }catch(Exception ex){
//                    ex.printStackTrace();
//                 }                               
//             }                
//         }
    }
    
    public ArrayList<String> getContractsSymbles(){
        return this.symbols;
    
    }
    
}
