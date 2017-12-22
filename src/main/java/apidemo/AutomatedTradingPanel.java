/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */
package apidemo;

import static com.ib.controller.Formats.fmt0;
import static apidemo.AccountInfoPanel.format;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JButton;


import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.Bar;

import apidemo.util.HtmlButton;
import apidemo.util.NewTabbedPanel;
import apidemo.util.NewTabbedPanel.NewTabPanel;
import apidemo.util.TCombo;
import apidemo.util.UpperField;
import apidemo.util.VerticalPanel;
import apidemo.util.VerticalPanel.StackPanel;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.Types;
import com.ib.controller.ApiController;
import static com.ib.controller.Formats.fmt0;
import com.ib.controller.MarketValueTag;
import com.ib.controller.Position;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import apidemo.util.NewTabbedPanel.INewTab;
import com.ib.client.StrategyHisDataRow;
import com.ib.client.StrategyMktDataRow;
import com.ib.controller.ApiController.IAccountHandler;
import java.util.Map;
import javax.swing.JTextArea;


//public class AutomatedTradingPanel extends JPanel {
public class AutomatedTradingPanel extends JPanel {

    private final Contract m_contract = new Contract();
    //my codes;
    // private final ArrayList<Contract> m_contract= new ArrayList<Contract>();

    private final NewTabbedPanel m_requestPanel = new NewTabbedPanel();
    private final NewTabbedPanel m_resultsPanel = new NewTabbedPanel();
    private final ArrayList<Contract> contracts = new ArrayList<>();
   // private static FileChoosePanel SELECTPANEL1 = new FileChoosePanel("Choose file from device");
   // private static final FileChoosePanel SELECTPANEL = new FileChoosePanel("Choose file from device");
    
    private final HashMap<String, Double> avgHistoricalPrices= new HashMap<>();
    private Double avgPrice;
    private final HashMap<String, Double> realTimePrices = new HashMap<>();
    private TopResultsPanel m_topResultPanel;
    
    final ContractPanel m_contractPanel = new ContractPanel(m_contract, contracts);
    

    /*
    String date_s = " 2011-01-18 00:00:00.0"; 
    SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss"); 
    Date date = dt.parse(date_s); 
    SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-mm-dd");
    System.out.println(dt1.format(date));
     */
    AutomatedTradingPanel() {
        
        m_requestPanel.addTab("Historical Data", new HistRequestPanel());
        m_requestPanel.addTab("Top Market Data", new TopRequestPanel());
        m_requestPanel.addTab("Real-time Bars", new RealtimeRequestPanel());
        

        //m_requestPanel.addTab( "Market Value", new TestPanel() );
        //m_requestPanel.addTab("Market Value", new AccountInfoPanel());
        m_requestPanel.addTab("Decisions", new DecisionPanel());
        m_requestPanel.addTab("Orders", new OrdersPanel());
        

        
      //  JScrollPane mvScroll = new JScrollPane(m_mktValTable);
      //  m_requestPanel.addTab ("Market Value", mvScroll);

        setLayout(new BorderLayout());
        add(m_requestPanel, BorderLayout.NORTH);
        add(m_resultsPanel);
    }



//    private boolean SellDecision() {
//
//        if (currentDate.compareTo(buyDate) == 3) {
//            sellSignal = true;
//        }
//        return sellSignal;
//    }
   private class TopRequestPanel extends JPanel {

        //final ContractPanel m_contractPanel = new ContractPanel(m_contract, contracts);
        //final FileChoosePanel selectPanel = new FileChoosePanel("Choose file from device");

        TopRequestPanel() {
            HtmlButton reqTop = new HtmlButton("Request Top Market Data") {
                @Override
                protected void actionPerformed() {
                    onTop();
                }
            };
            
            HtmlButton reqMultiTop = new HtmlButton("Request Data From File"){
                @Override
                protected void actionPerformed(){
                    onTopFile();
                }
            };
            

            VerticalPanel butPanel = new VerticalPanel();
            butPanel.add(reqTop);
            butPanel.add(reqMultiTop);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            //add(m_contractPanel);
            add(Box.createHorizontalStrut(20));
            add(butPanel);
            add(Box.createHorizontalStrut(20));
            //add(SELECTPANEL);
        }

//        protected void onTop() {
//            m_contractPanel.onOK();
//            if (m_topResultPanel == null) {
//                m_topResultPanel = new TopResultsPanel();
//                m_resultsPanel.addTab("Top Data", m_topResultPanel, true, true);
//            }
//
//            //m_topResultPanel.m_model.addRow(m_contract);
//            m_topResultPanel.m_model.addRows(contracts);
//        }
        
        protected void onTopFile(){
            m_contractPanel.onReqDFOK();
            
            if (m_topResultPanel == null) {
                m_topResultPanel = new TopResultsPanel();
                m_resultsPanel.addTab("Top Data", m_topResultPanel, true, true);
            }
            
            m_topResultPanel.m_model.addRows(m_contractPanel.getFileContracts());
        }
        protected void onTop() {
//            m_contractPanel.onOK();
//            ArrayList<String> symbols = SELECTPANEL.getContractsSymbles();
//            ArrayList<Contract> fileContract = new ArrayList<Contract>();
//            if (symbols != null) {
//                if (symbols.size() != 0) {
//                    for (int i = 0; i < symbols.size(); i++) {
//
//                        Contract fc = new Contract(m_contract.conid(), symbols.get(i), m_contract.getSecType(),
//                                m_contract.lastTradeDateOrContractMonth(), m_contract.strike(), m_contract.getRight(),
//                                m_contract.multiplier(), m_contract.exchange(), m_contract.currency(), m_contract.localSymbol(),
//                                m_contract.tradingClass(), m_contract.comboLegs(), m_contract.primaryExch(), m_contract.includeExpired(),
//                                m_contract.getSecIdType(), m_contract.secId());
//
//                        fileContract.add(fc);
//                    }
//                }
//            }

            if (m_topResultPanel == null) {
                m_topResultPanel = new TopResultsPanel();
                m_resultsPanel.addTab("Top Data", m_topResultPanel, true, true);
            }

            //m_topResultPanel.m_model.addRow( m_contract);
            //for(String symbols : symbols){
            //}
            m_topResultPanel.m_model.addRows(contracts);
            //m_topResultPanel.m_model.addRows(fileContract);
            // m_topResultPanel.m_model.addRows(m_contractList);
            // contractList ---> count  0 -> * 1, 2, 3, ....
        }
    }

    private class TopResultsPanel extends NewTabPanel {

        final TopModel m_model = new TopModel();
        final JTable m_tab = new TopTable(m_model);
        final TCombo<Types.MktDataType> m_typeCombo = new TCombo<Types.MktDataType>(Types.MktDataType.values());

        TopResultsPanel() {
            m_typeCombo.removeItemAt(0);

            JScrollPane scroll = new JScrollPane(m_tab);

            HtmlButton reqType = new HtmlButton("Go") {
                @Override
                protected void actionPerformed() {
                    onReqType();
                }
            };

            VerticalPanel butPanel = new VerticalPanel();
            butPanel.add("Market data type", m_typeCombo, reqType);

            setLayout(new BorderLayout());
            add(scroll);
            add(butPanel, BorderLayout.SOUTH);
        }

        /**
         * Called when the tab is first visited.
         */
        @Override
        public void activated() {
        }

        /**
         * Called when the tab is closed by clicking the X.
         */
        @Override
        public void closed() {
            m_model.desubscribe();
            m_topResultPanel = null;
        }

        void onReqType() {
            ApiDemo.INSTANCE.controller().reqMktDataType(m_typeCombo.getSelectedItem());
        }

        class TopTable extends JTable {

            public TopTable(TopModel model) {
                super(model);
            }

            @Override
            public TableCellRenderer getCellRenderer(int rowIn, int column) {
                TableCellRenderer rend = super.getCellRenderer(rowIn, column);
                m_model.color(rend, rowIn, getForeground());
                return rend;
            }
        }
    }

    
    private class HistRequestPanel extends JPanel {

        //final ContractPanel m_contractPanel = new ContractPanel(m_contract);
        //final ContractPanel m_contractPanel = new ContractPanel(m_contract, contracts);
        //final FileChoosePanel SELECTPANEL = new FileChoosePanel("Choose file from device");

        //  for( Contract i : m_contract){
        //symbol = symbol.trim();
        // }
        final UpperField m_end = new UpperField();
        final UpperField m_end1 = new UpperField();
        final UpperField m_duration = new UpperField();
        final TCombo<DurationUnit> m_durationUnit = new TCombo<DurationUnit>(DurationUnit.values());
//        final UpperField m_duration1 = new UpperField();
//        final TCombo<DurationUnit> m_durationUnit1 = new TCombo<DurationUnit>(DurationUnit.values());
        final TCombo<BarSize> m_barSize = new TCombo<BarSize>(BarSize.values());
        final TCombo<WhatToShow> m_whatToShow = new TCombo<WhatToShow>(WhatToShow.values());
        final JCheckBox m_rthOnly = new JCheckBox();
        
        //private final HashMap<String, Double> avgHistoricalPrices_local = new HashMap<>();
        


//        private javax.swing.JFileChooser fileChooser;
//        private javax.swing.JTextArea textarea;

        //LocalDateTime.now();
        HistRequestPanel() {
            ZonedDateTime ldt = ZonedDateTime.now();
            String currentTime = ldt.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String currentTime1 = currentTime.replace("-", "");
            currentTime1 = currentTime1 + " 16:00:00";
            m_end.setText(currentTime1);
            m_duration.setText("1");
            m_durationUnit.setSelectedItem(DurationUnit.WEEK);
   
//            ZonedDateTime ldt1 = ldt.minusDays(7);
//            String currentTime0 = ldt1.format(DateTimeFormatter.ISO_LOCAL_DATE);
//            String currentTime01 = currentTime0.replace("-", "");
//            currentTime01 = currentTime01 + " 16:00:00";
//            m_end1.setText(currentTime01);            
//            m_duration1.setText("1");
//            m_durationUnit1.setSelectedItem(DurationUnit.WEEK);
            m_barSize.setSelectedItem(BarSize._1_day);

            HtmlButton button = new HtmlButton("Request historical data") {
                @Override
                protected void actionPerformed() {
                    onHistorical();
                }
            };
            
            HtmlButton reqMultiPanel = new HtmlButton("Request Data From File"){
                @Override
                protected void actionPerformed(){
                   onReqHDFOK();
                }
            };
            
            HtmlButton strategyRun = new HtmlButton("Run Strategy"){
                @Override
                protected void actionPerformed(){
                    onRunStrategy();
                }           
            };
            
            HtmlButton strategyStop = new HtmlButton("Stop Strategy"){
               protected void actionPerformed(){
                  onStopStrategy();    
               }
            };


            VerticalPanel paramPanel = new VerticalPanel();
            paramPanel.add("End", m_end);
            paramPanel.add("Duration", m_duration);
            paramPanel.add("Duration unit", m_durationUnit);

//            paramPanel.add("End1", m_end1);
//            paramPanel.add("Duration1", m_duration1);
//            paramPanel.add("Duration unit1", m_durationUnit1);

            paramPanel.add("Bar size", m_barSize);
            paramPanel.add("What to show", m_whatToShow);
            paramPanel.add("RTH only", m_rthOnly);

            VerticalPanel butPanel = new VerticalPanel();
            butPanel.add(button);
            butPanel.add(reqMultiPanel);
            butPanel.add(strategyRun);
            butPanel.add(strategyStop);

            //butPanel.add(browseButton);
            JPanel rightPanel = new StackPanel();
            rightPanel.add(paramPanel);
            rightPanel.add(Box.createVerticalStrut(20));
            rightPanel.add(butPanel);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(m_contractPanel);
          //  add(Box.createHorizontalStrut(20));
            add(rightPanel);
          //  add(Box.createHorizontalStrut(20));
          // add(SELECTPANEL);
        }
        
        
//            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//            add(m_contractPanel);
//            add(Box.createHorizontalStrut(20));
//            add(rightPanel);
//            add(Box.createHorizontalStrut(20));
//            add(SELECTPANEL);

//        protected void onHistorical() {
//            m_contractPanel.onOK();
//            ArrayList<String> symbols = SELECTPANEL.getSymbols();
//            ArrayList<Contract> fileContract = new ArrayList<Contract>();
//            if (symbols != null) {
//                if (symbols.size() != 0) {
//                    for (int i = 0; i < symbols.size(); i++) {
//
//                        Contract fc = new Contract(m_contract.conid(), symbols.get(i), m_contract.getSecType(),
//                                m_contract.lastTradeDateOrContractMonth(), m_contract.strike(), m_contract.getRight(),
//                                m_contract.multiplier(), m_contract.exchange(), m_contract.currency(), m_contract.localSymbol(),
//                                m_contract.tradingClass(), m_contract.comboLegs(), m_contract.primaryExch(), m_contract.includeExpired(),
//                                m_contract.getSecIdType(), m_contract.secId());
//
//                        fileContract.add(fc);
//                    }
//                }
//            }
//
//            contracts.addAll(fileContract);
//            for (int i = 0; i < contracts.size(); i++) {
//                BarResultsPanel panel = new BarResultsPanel(true);
//                ApiDemo.INSTANCE.controller().reqHistoricalData(contracts.get(i), m_end.getText(), m_duration.getInt(), m_durationUnit.getSelectedItem(), m_barSize.getSelectedItem(), m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
//                m_resultsPanel.addTab("Historical " + contracts.get(i).symbol(), panel, true, true);
//
////                BarResultsPanel panel1 = new BarResultsPanel(true);
////                ApiDemo.INSTANCE.controller().reqHistoricalData(contracts.get(i), m_end1.getText(), m_duration1.getInt(), m_durationUnit1.getSelectedItem(), m_barSize.getSelectedItem(), m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel1);
////                m_resultsPanel.addTab("Historical1 " + contracts.get(i).symbol(), panel1, true, true);
//
//                /* my codes begin */
//                //final ArrayList<Bar> m_rows = new ArrayList<Bar>();
//                //public double close() 	{ return m_close; }
//                Double sumPrice= 0.0;
//                
//                //if (panel.m_rows.isEmpty() != true) {
//                int ii; 
//                ii=panel.m_rows.size();
//                    for (int j = 0; j < ii; j++) {
//                        sumPrice = sumPrice + panel.m_rows.get(j).close();
//                    }
//                    //avgHistoricalPrice[i] = sumPrice / panel.m_rows.size();
//                    
//                    avgPrice = sumPrice / (double)ii;
//                    double a;
//                    a=sumPrice / (double)ii;
//                    System.out.println(avgPrice);
//                    System.out.println(a);
//                    avgHistoricalPrices.put(contracts.get(i).symbol(), avgPrice);
//                    System.out.println(avgHistoricalPrices.get(contracts.get(i).symbol()));
//                //}
//            }
//
//            /* my codes end */
//        }
        protected void onHistorical() {
            m_contractPanel.onOK();
            
            for(int i = 0; i < contracts.size(); i++){
                MarketDataPanel.BarResultsPanel panel = new MarketDataPanel.BarResultsPanel(true);
                
                ApiDemo.INSTANCE.controller().reqHistoricalData(contracts.get(i), m_end.getText(), m_duration.getInt(), m_durationUnit.getSelectedItem(), m_barSize.getSelectedItem(), m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
                m_resultsPanel.addTab("Historical " + contracts.get(i).symbol(), panel, true, true);
                
            }
            //ApiDemo.INSTANCE.controller().reqHistoricalData(m_contract, m_end.getText(), m_duration.getInt(), m_durationUnit.getSelectedItem(), m_barSize.getSelectedItem(), m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);          
        }
        
        protected void onReqHDFOK(){
        	m_resultsPanel.clear();
            m_contractPanel.onReqDFOK();
            ArrayList<Contract> contracts = m_contractPanel.getFileContracts();
            if(contracts != null && !contracts.isEmpty()){
                for(int i = 0 ; i < contracts.size(); i++){
                    MarketDataPanel.BarResultsPanel panel = new MarketDataPanel.BarResultsPanel(true);
                    ApiDemo.INSTANCE.controller().reqHistoricalData(contracts.get(i), m_end.getText(), m_duration.getInt(), m_durationUnit.getSelectedItem(), m_barSize.getSelectedItem(), m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
                    m_resultsPanel.addTab("Historical FF " + contracts.get(i).symbol(), panel, true, true);
              }          
           }          
        }
        
        protected void onStopStrategy(){
           ApiDemo.INSTANCE.controller().setRunStrategy(false);
        }
        
        protected void onRunStrategy(){
            
            ApiDemo.INSTANCE.controller().setRunStrategy(true);
            ArrayList<Contract> contracts = m_contractPanel.getFileContracts();
            //send request to get Previous three days stock price data 
            Contract hisContract = new Contract();
         
            DateFormat dataFormat = new SimpleDateFormat("yyyyMMDD hh:mm:ss");
            Date date = new Date();
            String endTime = dataFormat.format(date);
          
            if(contracts != null && !contracts.isEmpty()){
                
                Contract defContract = contracts.get(0);
               // hisContract = defContract.clone();
                hisContract.symbol(defContract.symbol());
                hisContract.secType( defContract.secType() ); 
                hisContract.lastTradeDateOrContractMonth( defContract.lastTradeDateOrContractMonth() ); 
                hisContract.strike( defContract.strike() ); 
                hisContract.right( defContract.right()); 
                hisContract.multiplier( defContract.multiplier() ); 
                hisContract.exchange( defContract.exchange());
                hisContract.primaryExch( defContract.primaryExch());
                hisContract.currency( defContract.currency() ); 
                hisContract.localSymbol( defContract.localSymbol());
                hisContract.tradingClass( defContract.tradingClass() );  
                StrategyHisDataRow hisBar = new StrategyHisDataRow(true);
                ApiDemo.INSTANCE.controller().reqHistoricalDataStrategy(contracts.get(0), endTime, 3 , "Days", BarSize._1_day.toString(), WhatToShow.TRADES.toString(), false, hisBar);
                
                TopResultsPanel straPanel = new TopResultsPanel();
                m_resultsPanel.addTab("Strategy For " + defContract.symbol(), straPanel, true, true);
                StrategyMktDataRow row = new StrategyMktDataRow(defContract.symbol());                 
                ApiDemo.INSTANCE.controller().reqTopMktDataStrategy(defContract, "", false, row);       
            }
            //if the price of current stock price 
        }
    }

    private class RealtimeRequestPanel extends JPanel {

        //final ContractPanel m_contractPanel = new ContractPanel(m_contract);
        //final ContractPanel m_contractPanel = new ContractPanel(m_contract, contracts);
        //final FileChoosePanel SELECTPANEL1 = SELECTPANEL;
        final TCombo<WhatToShow> m_whatToShow = new TCombo<WhatToShow>(WhatToShow.values());
        final JCheckBox m_rthOnly = new JCheckBox();
        
        
        //HashMap<String, Boolean> buyDecisions = new HashMap<>();

        RealtimeRequestPanel() {
            HtmlButton button = new HtmlButton("Request real-time bars") {
                @Override
                protected void actionPerformed() {
                    onRealTime();
                }
            };
            
            HtmlButton reqMultiReal = new HtmlButton("Request Data From File"){
                @Override
                protected void actionPerformed(){
                   onReqRDFOK();
                }
            };
            

            VerticalPanel paramPanel = new VerticalPanel();
            paramPanel.add("What to show", m_whatToShow);
            paramPanel.add("RTH only", m_rthOnly);

            VerticalPanel butPanel = new VerticalPanel();
            butPanel.add(button);
            butPanel.add(reqMultiReal);

            JPanel rightPanel = new StackPanel();
            rightPanel.add(paramPanel);
            rightPanel.add(Box.createVerticalStrut(20));
            rightPanel.add(butPanel);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
           // add(m_contractPanel);
            add(Box.createHorizontalStrut(20));
            add(rightPanel);
            //add(Box.createHorizontalStrut(20));
            //add(SELECTPANEL);
        }

        protected void onRealTime() {
           /* m_contractPanel.onOK();
            //BarResultsPanel panel = new BarResultsPanel(false);
            // ApiDemo.INSTANCE.controller().reqRealTimeBars(m_contract, m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
            //m_resultsPanel.addTab("Real-time " + m_contract.symbol(), panel, true, true);
            //realTimePrice = panel.m_rows.get(0).close();
            ArrayList<String> symbols = SELECTPANEL.getContractsSymbles();
            ArrayList<Contract> fileContract = new ArrayList<Contract>();
            if (symbols != null) {
                if (symbols.size() != 0) {
                    for (int i = 0; i < symbols.size(); i++) {

                        Contract fc = new Contract(m_contract.conid(), symbols.get(i), m_contract.getSecType(),
                                m_contract.lastTradeDateOrContractMonth(), m_contract.strike(), m_contract.getRight(),
                                m_contract.multiplier(), m_contract.exchange(), m_contract.currency(), m_contract.localSymbol(),
                                m_contract.tradingClass(), m_contract.comboLegs(), m_contract.primaryExch(), m_contract.includeExpired(),
                                m_contract.getSecIdType(), m_contract.secId());

                        fileContract.add(fc);
                    }
                }
            }

            contracts.addAll(fileContract);*/
            for (int i = 0; i < contracts.size(); i++) {
                BarResultsPanel panel = new BarResultsPanel(false);
                ApiDemo.INSTANCE.controller().reqRealTimeBars(contracts.get(i), m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
                m_resultsPanel.addTab("Real-time " + contracts.get(i).symbol(), panel, true, true);
                
               // RealTimeBars myData = new RealTimeBars()；
               //realTimePrices.put(contracts.get(i).symbol(), 100.0);
               
            }
        }
        
        protected void onReqRDFOK(){
            m_contractPanel.onReqDFOK();
            ArrayList<Contract> contracts = m_contractPanel.getFileContracts();
            if(contracts != null && !contracts.isEmpty()){
                for(int i = 0 ; i < contracts.size(); i++){
                BarResultsPanel panel = new BarResultsPanel(false);
                ApiDemo.INSTANCE.controller().reqRealTimeBars(contracts.get(i), m_whatToShow.getSelectedItem(), m_rthOnly.isSelected(), panel);
                m_resultsPanel.addTab("Real-time " + contracts.get(i).symbol(), panel, true, true);
              }          
           }          
        }
        
        //    private HashMap<String, boolean> BuyDecision() {
//        HashMap<String, boolean> decisions;
//        if (avgHistoricalPrice != -1 && realTimePrice != -1 && realTimePrice < avgHistoricalPrice) {
//            buySignal = true;
//        }
//        return decisions;
//    }
        
                
//        public HashMap<String, Double> getRealTimePrices(){
//            return realTimePrices;      
//        }
        
//        public HashMap<String, Boolean> onBuy() {
//            
//            realTimePrice
//            
//             return buyDecisions;
//         }

    }

    static class BarResultsPanel extends NewTabPanel implements IHistoricalDataHandler, IRealTimeBarHandler {

        final BarModel m_model = new BarModel();
        final ArrayList<Bar> m_rows = new ArrayList<Bar>();
        final boolean m_historical;
        final Chart m_chart = new Chart(m_rows);

        BarResultsPanel(boolean historical) {
            m_historical = historical;

            JTable tab = new JTable(m_model);
            JScrollPane scroll = new JScrollPane(tab) {
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    d.width = 500;
                    return d;
                }
            };

            JScrollPane chartScroll = new JScrollPane(m_chart);

            setLayout(new BorderLayout());
            add(scroll, BorderLayout.WEST);
            add(chartScroll, BorderLayout.CENTER);
        }

        /**
         * Called when the tab is first visited.
         */
        @Override
        public void activated() {
        }

        /**
         * Called when the tab is closed by clicking the X.
         */
        @Override
        public void closed() {
            if (m_historical) {
                ApiDemo.INSTANCE.controller().cancelHistoricalData(this);
            } else {
                ApiDemo.INSTANCE.controller().cancelRealtimeBars(this);
            }
        }

        @Override
        public void historicalData(Bar bar, boolean hasGaps) {
            m_rows.add(bar);
        }

        @Override
        public void historicalDataEnd() {
            fire();
        }

        @Override
        public void realtimeBar(Bar bar) {
            m_rows.add(bar);
            fire();
        }

        private void fire() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    m_model.fireTableRowsInserted(m_rows.size() - 1, m_rows.size() - 1);
                    m_chart.repaint();
                }
            });
        }

        class BarModel extends AbstractTableModel {

            @Override
            public int getRowCount() {
                return m_rows.size();
            }

            @Override
            public int getColumnCount() {
                return 7;
            }

            @Override
            public String getColumnName(int col) {
                switch (col) {
                    case 0:
                        return "Date/time";
                    case 1:
                        return "Open";
                    case 2:
                        return "High";
                    case 3:
                        return "Low";
                    case 4:
                        return "Close";
                    case 5:
                        return "Volume";
                    case 6:
                        return "WAP";
                    default:
                        return null;
                }
            }

            @Override
            public Object getValueAt(int rowIn, int col) {
                Bar row = m_rows.get(rowIn);
                switch (col) {
                    case 0:
                        return row.formattedTime();
                    case 1:
                        return row.open();
                    case 2:
                        return row.high();
                    case 3:
                        return row.low();
                    case 4:
                        return row.close();
                    case 5:
                        return row.volume();
                    case 6:
                        return row.wap();
                    default:
                        return null;
                }
            }
        }
    }

    private class OrdersPanel extends JPanel {

        private OrdersModel m_model = new OrdersModel();
        private JTable m_table = new JTable(m_model);

        OrdersPanel() {

            HtmlButton ticket = new HtmlButton("Place New Order") {
                @Override
                public void actionPerformed() {
                    onPlaceOrder();
                }
            };

            JPanel buts = new VerticalPanel();
            buts.add(ticket);

            setLayout(new BorderLayout());
            add(buts, BorderLayout.WEST);
            //add(scroll);
        }

        protected void onCancel() {
            OrderRow order = getSelectedOrder();
            if (order != null) {
                ApiDemo.INSTANCE.controller().cancelOrder(order.m_order.orderId());
            }
        }

        private OrderRow getSelectedOrder() {
            int i = m_table.getSelectedRow();
            return i != -1 ? m_model.get(i) : null;
        }

        private void onPlaceOrder() {
            TicketDlg dlg = new TicketDlg(null, null);
            dlg.setVisible(true);
        }

        class OrdersModel extends AbstractTableModel implements ApiController.ILiveOrderHandler {

            private HashMap<Long, OrderRow> m_map = new HashMap<Long, OrderRow>();
            private ArrayList<OrderRow> m_orders = new ArrayList<OrderRow>();

            @Override
            public int getRowCount() {
                return m_orders.size();
            }

            public void clear() {
                m_orders.clear();
                m_map.clear();
            }

            public OrderRow get(int i) {
                return m_orders.get(i);
            }

            @Override
            public void openOrder(Contract contract, Order order, OrderState orderState) {
                OrderRow full = m_map.get(order.permId());

                if (full != null) {
                    full.m_order = order;
                    full.m_state = orderState;
                    fireTableDataChanged();
                } else if (shouldAdd(contract, order, orderState)) {
                    full = new OrderRow(contract, order, orderState);
                    add(full);
                    m_map.put(order.permId(), full);
                    fireTableDataChanged();
                }
            }

            protected boolean shouldAdd(Contract contract, Order order, OrderState orderState) {
                return true;
            }

            protected void add(OrderRow full) {
                m_orders.add(full);
            }

            @Override
            public void openOrderEnd() {
            }

            @Override
            public void orderStatus(int orderId, OrderStatus status, double filled, double remaining, double avgFillPrice, long permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
                OrderRow full = m_map.get(permId);
                if (full != null) {
                    full.m_state.status(status);
                }
                fireTableDataChanged();
            }

            @Override
            public int getColumnCount() {
                return 9;
            }

            @Override
            public String getColumnName(int col) {
                switch (col) {
                    case 0:
                        return "Perm ID";
                    case 1:
                        return "Client ID";
                    case 2:
                        return "Order ID";
                    case 3:
                        return "Account";
                    case 4:
                        return "ModelCode";
                    case 5:
                        return "Action";
                    case 6:
                        return "Quantity";
                    case 7:
                        return "Contract";
                    case 8:
                        return "Status";
                    default:
                        return null;
                }
            }

            @Override
            public Object getValueAt(int row, int col) {
                OrderRow fullOrder = m_orders.get(row);
                Order order = fullOrder.m_order;
                switch (col) {
                    case 0:
                        return order.permId();
                    case 1:
                        return order.clientId();
                    case 2:
                        return order.orderId();
                    case 3:
                        return order.account();
                    case 4:
                        return order.modelCode();
                    case 5:
                        return order.action();
                    case 6:
                        return order.totalQuantity();
                    case 7:
                        return fullOrder.m_contract.description();
                    case 8:
                        return fullOrder.m_state.status();
                    default:
                        return null;
                }
            }

            @Override
            public void handle(int orderId, int errorCode, String errorMsg) {
            }
        }

        class OrderRow {

            Contract m_contract;
            Order m_order;
            OrderState m_state;

            OrderRow(Contract contract, Order order, OrderState state) {
                m_contract = contract;
                m_order = order;
                m_state = state;
            }
        }

        class Key {

            int m_clientId;
            int m_orderId;

            Key(int clientId, int orderId) {
                m_clientId = clientId;
                m_orderId = orderId;
            }
        }
    }
    
    
    //////////////////////////////////////////////////////////////////////////////
    ///  Decision panel
    //////////////////////////////////////////////////////////////////////////////
    private class DecisionPanel extends JPanel {
        
        //final ShowResultsPanel m_showResults = new ShowResultsPanel("Show results");
        private JTextArea log;
        private HashMap<String, Boolean> decisions = new HashMap<>();
        
        DecisionPanel() {

            HtmlButton compareRealHistoricalButton = new HtmlButton("Real Time VS Historical") {
                @Override
                public void actionPerformed() {
                    onCompareRealHistorical();
                }
            };
            log = new JTextArea(5, 20);
            log.setEditable(false);
            JScrollPane logScrollPane = new JScrollPane(log);

            VerticalPanel butPanel = new VerticalPanel();
            butPanel.add(compareRealHistoricalButton);

            //butPanel.add(browseButton);
            JPanel rightPanel = new StackPanel();
            rightPanel.add(butPanel);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(rightPanel);
            add(Box.createHorizontalStrut(20));
            add(logScrollPane);          
        }

        private void onCompareRealHistorical() {
            //decisions=null;
            for (Map.Entry<String, Double> i : avgHistoricalPrices.entrySet()) {
                for (Map.Entry<String, Double> j : realTimePrices.entrySet()) {
                    if (i.getKey().equalsIgnoreCase(j.getKey()) && (i.getValue().compareTo(j.getValue()) > 0)) {
                        decisions.put(i.getKey(), Boolean.TRUE);
                        int size = decisions.size();
                        
                    }
                }
            }
            
            if (!decisions.isEmpty()){
                for (Map.Entry<String, Boolean> i : decisions.entrySet()) {
                    log.append(i.getKey() + "\n");
                }
            } else{
                log.append("No Stocks to Buy.");
            }
        }
    }
}








//public class AccountInfoPanel extends JPanel implements NewTabbedPanel.INewTab, ApiController.IAccountHandler {
//
//    private DefaultListModel m_acctList = new DefaultListModel();
//    private JList m_accounts = new JList(m_acctList);
//    private String m_selAcct = "";
//    private MarginModel m_marginModel = new MarginModel();
//    private JTable m_marginTable = new Table(m_marginModel);
//    private PortfolioModel m_portfolioModel = new PortfolioModel();
//    private JTable m_portfolioTable = new Table(m_portfolioModel);
//    private MktValModel m_mktValModel = new MktValModel();
//    private JTable m_mktValTable = new Table(m_mktValModel, 2);
//    private JLabel m_lastUpdated = new JLabel();
//
//    AccountInfoPanel() {
//        m_lastUpdated.setHorizontalAlignment(SwingConstants.RIGHT);
//
//        m_accounts.setPreferredSize(new Dimension(10000, 100));
//        JScrollPane acctScroll = new JScrollPane(m_accounts);
//        acctScroll.setBorder(new TitledBorder("Select Account"));
//
//        JScrollPane marginScroll = new JScrollPane(m_marginTable);
//        JScrollPane mvScroll = new JScrollPane(m_mktValTable);
//
//        JScrollPane portScroll = new JScrollPane(m_portfolioTable);
//
//        NewTabbedPanel tabbedPanel = new NewTabbedPanel();
//        //tabbedPanel.addTab( "Balances and Margin", marginScroll);
//        tabbedPanel.addTab("Market Value", mvScroll);
//        tabbedPanel.addTab("Portfolio", portScroll);
//        //tabbedPanel.addTab( "Account Summary", new AccountSummaryPanel() );
//        //tabbedPanel.addTab( "Market Value Summary", new MarketValueSummaryPanel() );
//        tabbedPanel.addTab("Positions (all accounts)", new PositionsPanel());
//
//        setLayout(new BorderLayout());
//        //add( acctScroll, BorderLayout.NORTH);
//        add(tabbedPanel);
//        //add( m_lastUpdated, BorderLayout.SOUTH);
//
//        m_accounts.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                onChanged();
//            }
//        });
//    }
//

//
//    /**
//     * Receive position.
//     */
//    public synchronized void updatePortfolio(Position position) {
//        if (position.account().equals(m_selAcct)) {
//            m_portfolioModel.update(position);
//        }
//    }
//
//    /**
//     * Receive time of last update.
//     */
//    public void accountTime(String timeStamp) {
//        m_lastUpdated.setText("Last updated: " + timeStamp + "       ");
//    }
//
//    public void accountDownloadEnd(String account) {
//    }
//
//    private class MarginModel extends AbstractTableModel {
//
//        HashMap<MarginRowKey, MarginRow> m_map = new HashMap<MarginRowKey, MarginRow>();
//        ArrayList<MarginRow> m_list = new ArrayList<MarginRow>();
//
//        void clear() {
//            m_map.clear();
//            m_list.clear();
//        }
//
//        public void handle(String tag, String value, String currency, String account) {
//            // useless
//            if (tag.equals("Currency")) {
//                return;
//            }
//
//            int type = 0; // 0=whole acct; 1=securities; 2=commodities
//
//            // "Securities" segment?
//            if (tag.endsWith("-S")) {
//                tag = tag.substring(0, tag.length() - 2);
//                type = 1;
//            } // "Commodities" segment?
//            else if (tag.endsWith("-C")) {
//                tag = tag.substring(0, tag.length() - 2);
//                type = 2;
//            }
//
//            MarginRowKey key = new MarginRowKey(tag, currency);
//            MarginRow row = m_map.get(key);
//
//            if (row == null) {
//                // don't add new rows with a value of zero
//                if (isZero(value)) {
//                    return;
//                }
//
//                row = new MarginRow(tag, currency);
//                m_map.put(key, row);
//                m_list.add(row);
//                Collections.sort(m_list);
//            }
//
//            switch (type) {
//                case 0:
//                    row.m_val = value;
//                    break;
//                case 1:
//                    row.m_secVal = value;
//                    break;
//                case 2:
//                    row.m_comVal = value;
//                    break;
//            }
//
//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    fireTableDataChanged();
//                }
//            });
//        }
//
//        @Override
//        public int getRowCount() {
//            return m_list.size();
//        }
//
//        @Override
//        public int getColumnCount() {
//            return 4;
//        }
//
//        @Override
//        public String getColumnName(int col) {
//            switch (col) {
//                case 0:
//                    return "Tag";
//                case 1:
//                    return "Account Value";
//                case 2:
//                    return "Securities Value";
//                case 3:
//                    return "Commodities Value";
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public Object getValueAt(int rowIn, int col) {
//            MarginRow row = m_list.get(rowIn);
//
//            switch (col) {
//                case 0:
//                    return row.m_tag;
//                case 1:
//                    return format(row.m_val, row.m_currency);
//                case 2:
//                    return format(row.m_secVal, row.m_currency);
//                case 3:
//                    return format(row.m_comVal, row.m_currency);
//                default:
//                    return null;
//            }
//        }
//    }
//
//    private class MarginRow implements Comparable<MarginRow> {
//
//        String m_tag;
//        String m_currency;
//        String m_val;
//        String m_secVal;
//        String m_comVal;
//
//        MarginRow(String tag, String cur) {
//            m_tag = tag;
//            m_currency = cur;
//        }
//
//        @Override
//        public int compareTo(MarginRow o) {
//            return m_tag.compareTo(o.m_tag);
//        }
//    }
//
//    private class MarginRowKey {
//
//        String m_tag;
//        String m_currency;
//
//        public MarginRowKey(String key, String currency) {
//            m_tag = key;
//            m_currency = currency;
//        }
//
//        @Override
//        public int hashCode() {
//            int cur = m_currency != null ? m_currency.hashCode() : 0;
//            return m_tag.hashCode() + cur;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            MarginRowKey other = (MarginRowKey) obj;
//            return m_tag.equals(other.m_tag)
//                    && (m_currency == null && other.m_currency == null || m_currency != null && m_currency.equals(other.m_currency));
//        }
//    }
//
//    /**
//     * Shared with ExercisePanel.
//     */
//    class PortfolioModel extends AbstractTableModel {
//
//        private HashMap<Integer, Position> m_portfolioMap = new HashMap<Integer, Position>();
//        private ArrayList<Integer> m_positions = new ArrayList<Integer>(); // must store key because Position is overwritten
//
//        void clear() {
//            m_positions.clear();
//            m_portfolioMap.clear();
//        }
//
//        Position getPosition(int i) {
//            return m_portfolioMap.get(m_positions.get(i));
//        }
//
//        public void update(Position position) {
//            // skip fake FX positions
//            if (position.contract().secType() == Types.SecType.CASH) {
//                return;
//            }
//
//            if (!m_portfolioMap.containsKey(position.conid()) && position.position() != 0) {
//                m_positions.add(position.conid());
//            }
//            m_portfolioMap.put(position.conid(), position);
//            fireTableDataChanged();
//        }
//
//        @Override
//        public int getRowCount() {
//            return m_positions.size();
//        }
//
//        @Override
//        public int getColumnCount() {
//            return 7;
//        }
//
//        @Override
//        public String getColumnName(int col) {
//            switch (col) {
//                case 0:
//                    return "Description";
//                case 1:
//                    return "Position";
//                case 2:
//                    return "Price";
//                case 3:
//                    return "Value";
//                case 4:
//                    return "Avg Cost";
//                case 5:
//                    return "Unreal Pnl";
//                case 6:
//                    return "Real Pnl";
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public Object getValueAt(int row, int col) {
//            Position pos = getPosition(row);
//            switch (col) {
//                case 0:
//                    return pos.contract().description();
//                case 1:
//                    return pos.position();
//                case 2:
//                    return pos.marketPrice();
//                case 3:
//                    return format("" + pos.marketValue(), null);
//                case 4:
//                    return pos.averageCost();
//                case 5:
//                    return pos.unrealPnl();
//                case 6:
//                    return pos.realPnl();
//                default:
//                    return null;
//            }
//        }
//    }
//
//    private boolean isZero(String value) {
//        try {
//            return Double.parseDouble(value) == 0;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     * If val is a number, format it with commas and no decimals.
//     */
//    String format(String val, String currency) {
//        if (val == null || val.length() == 0) {
//            return null;
//        }
//
//        try {
//            double dub = Double.parseDouble(val);
//            val = fmt0(dub);
//        } catch (Exception e) {
//        }
//
//        return currency != null && currency.length() > 0
//                ? val + " " + currency : val;
//    }
//
//    /**
//     * Table where first n columns are left-justified, all other columns are
//     * right-justified.
//     */
//    class Table extends JTable {
//
//        private int m_n;
//
//        public Table(AbstractTableModel model) {
//            this(model, 1);
//        }
//
//        public Table(AbstractTableModel model, int n) {
//            super(model);
//            m_n = n;
//        }
//
//        @Override
//        public TableCellRenderer getCellRenderer(int row, int col) {
//            TableCellRenderer rend = super.getCellRenderer(row, col);
//            ((JLabel) rend).setHorizontalAlignment(col < m_n ? SwingConstants.LEFT : SwingConstants.RIGHT);
//            return rend;
//        }
//    }
//}




