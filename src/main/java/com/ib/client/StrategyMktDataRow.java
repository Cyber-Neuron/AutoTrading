/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.client;

/**
 *
 * @author jyang
 */
public class StrategyMktDataRow {
    		String symbol;
		String m_description;
		double m_bid;
		double m_ask;
		double m_last;
		long m_lastTime;		
		//double m_close;
		//int m_volume;
		//boolean m_frozen;
		
	public StrategyMktDataRow(String symbol) {
	    this.symbol = symbol;
            m_description = symbol;
	}
        
        public void setBidPrice(double price){
            this.m_bid = price;
        }
        
        public void setAskPrice(double price){
            this.m_ask = price;
        }
        
        public void setLastPrice(double price){
            this.m_last = price;
        }
    
}
