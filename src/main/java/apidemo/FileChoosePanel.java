/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apidemo;

import com.ib.client.Contract;
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
public class FileChoosePanel extends JPanel implements ActionListener{
    
    private String Panel_Name;
    
    private JButton openButton;
    private JTextArea log;
    private JFileChooser fChoose;
    private JButton loadButton;
    private File selectedFile;
    private ArrayList<String> symbols;
    protected ArrayList<Contract> fContracts;
    
    public FileChoosePanel(){
        this.symbols = new ArrayList<>();
        log = new JTextArea(6, 40);
     
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        
        fChoose = new JFileChooser();
        openButton = new JButton("Open a file");
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
        this.fContracts = new ArrayList<Contract>();
    }
    
    public FileChoosePanel(String panelname){
        this();
        setPanelName(panelname);
    }
    
    public void setPanelName(String name){
        this.Panel_Name = name;
    }
    
    public void actionPerformed(ActionEvent e){
         if(e.getSource() == openButton){
              int returnVal = fChoose.showOpenDialog(FileChoosePanel.this);
              
              if(returnVal == JFileChooser.APPROVE_OPTION){
                
                 File file = fChoose.getSelectedFile();
                 file = new File("/home/dan/Workspace/AutoTrading/companies.csv");
                 selectedFile = file;                 
                 if(selectedFile != null && selectedFile.exists()){
                     try{
                        log.append("**************************************************");
                        log.append("Opening: " + file.getName() + "\n");
                        log.append(file.getAbsolutePath() + "\n");
                        FileReader fr = new FileReader(selectedFile.getAbsolutePath());
                        BufferedReader br = new BufferedReader(fr);
                        String content = "";
                        if (symbols == null)
                            this.symbols = new ArrayList<String>();
                        else
                            this.symbols.clear();
                        while((content = br.readLine()) != null){
                            
                             this.symbols.add(content.trim().toUpperCase());
                             log.append(content.trim().toUpperCase() + "\n");
                        }
                        br.close();
                        fr.close();
                        log.append("**************************************************");
                     }catch(Exception ex){
                         ex.printStackTrace();
                     }
                 }
                 
             }else{
                 log.append("Open command cancelled by user." + "\n");
              }
              log.setCaretPosition(log.getDocument().getLength());
         }
    }
    
    public ArrayList<String> getSymbols(){
        return this.symbols;  
    }
   
    public void setFileContracts(ArrayList<Contract> contracts){
       this.fContracts = contracts;
    }
    
    public ArrayList<Contract> getFileContracts(){
       return this.fContracts;
    }
}
