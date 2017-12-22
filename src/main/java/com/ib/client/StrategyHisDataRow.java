/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.client;

import com.ib.controller.Bar;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author jyang
 */
public class StrategyHisDataRow {
        private ArrayList<Bar> m_rows;
        private boolean m_historical;
        private String symbol;
        
        public StrategyHisDataRow(boolean historicalData){
           m_historical = historicalData;
           m_rows = new ArrayList<Bar>();
        }
        
        public void addBar(Bar bar){
           m_rows.add(bar);
        }
        
        public void historicalDataEnd() {
            fire();
        }
        
        public void setSymbol(String symbol){
           this.symbol = symbol;
        }
        
        public void fire(){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //m_model.fireTableRowsInserted(m_rows.size() - 1, m_rows.size() - 1);
                    //m_chart.repaint();
                }
            });
        }
}
