/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package apidemo;

import java.util.*;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.ib.client.Contract;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;

import apidemo.util.TCombo;
import apidemo.util.UpperField;
import apidemo.util.VerticalPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
public class ContractPanel extends JPanel {
	protected UpperField m_symbol = new UpperField();
	protected TCombo<SecType> m_secType = new TCombo<SecType>( SecType.values() );
	protected UpperField m_lastTradeDateOrContractMonth = new UpperField();
	protected UpperField m_strike = new UpperField();
	protected TCombo<Right> m_right = new TCombo<Right>( Right.values() );
	protected UpperField m_multiplier = new UpperField();
	protected UpperField m_exchange = new UpperField();
	protected UpperField m_compExch = new UpperField();
	protected UpperField m_currency = new UpperField();
	protected UpperField m_localSymbol = new UpperField();
	protected UpperField m_tradingClass = new UpperField();
        protected FileChoosePanel fileChoosePanel = new FileChoosePanel();
        
	private Contract m_contract;
        private ArrayList<Contract> contractList;
	ContractPanel(Contract c) {
		m_contract = c;

		if (c.secType() == SecType.None) {
			m_symbol.setText( "IBM");
			m_secType.setSelectedItem( SecType.STK);
			m_exchange.setText( "SMART");
			m_compExch.setText( "ISLAND");
			m_currency.setText( "USD");
		}
		else {
			m_symbol.setText( m_contract.symbol());
			m_secType.setSelectedItem( m_contract.secType() );
			m_lastTradeDateOrContractMonth.setText( m_contract.lastTradeDateOrContractMonth());
			m_strike.setText( "" + m_contract.strike() );
			m_right.setSelectedItem( m_contract.right() ); 
			m_multiplier.setText( m_contract.multiplier() );
			m_exchange.setText( m_contract.exchange());
			m_compExch.setText( m_contract.primaryExch() );
			m_currency.setText( m_contract.currency());
			m_localSymbol.setText( m_contract.localSymbol());
			m_tradingClass.setText( m_contract.tradingClass() );
		}
		
		VerticalPanel p = new VerticalPanel();
    	p.add( "Symbol", m_symbol);
    	p.add( "Sec type", m_secType);
    	p.add( "Last trade date or contract month", m_lastTradeDateOrContractMonth);
    	p.add( "Strike", m_strike);
    	p.add( "Put/call", m_right);
    	p.add( "Multiplier", m_multiplier);
    	p.add( "Exchange", m_exchange);
    	p.add( "Comp. Exch.", m_compExch);
    	p.add( "Currency", m_currency);
    	p.add( "Local symbol", m_localSymbol);
    	p.add( "Trading class", m_tradingClass);
    	
//    	setLayout( new BorderLayout() );
//    	add( p);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    	add( p);
        add(Box.createHorizontalStrut(20));
        add(fileChoosePanel);
	}
        
        ContractPanel(Contract defaultCon, ArrayList<Contract> contracts){
            m_contract = defaultCon;
            contractList = contracts;
            if(contractList.size() == 0){         
                m_symbol.setText( "IBM");
		m_secType.setSelectedItem( SecType.STK);
		m_exchange.setText( "SMART");
		m_compExch.setText( "ISLAND");
		m_currency.setText( "USD");
            }else{
                String contractSymbols = "";
                for(Contract contract : contracts){
                   contractSymbols = contractSymbols + contract.symbol() + ",";
                }
                contractSymbols = contractSymbols.substring(0, contractSymbols.length() - 1);
                m_symbol.setText(contractSymbols);
			m_secType.setSelectedItem( m_contract.secType() );
			m_lastTradeDateOrContractMonth.setText( m_contract.lastTradeDateOrContractMonth());
			m_strike.setText( "" + m_contract.strike() );
			m_right.setSelectedItem( m_contract.right() ); 
			m_multiplier.setText( m_contract.multiplier() );
			m_exchange.setText( m_contract.exchange());
			m_compExch.setText( m_contract.primaryExch() );
			m_currency.setText( m_contract.currency());
			m_localSymbol.setText( m_contract.localSymbol());
			m_tradingClass.setText( m_contract.tradingClass() );
                
            }
            VerticalPanel p = new VerticalPanel();
    	p.add( "Symbol", m_symbol);
    	p.add( "Sec type", m_secType);
    	p.add( "Last trade date or contract month", m_lastTradeDateOrContractMonth);
    	p.add( "Strike", m_strike);
    	p.add( "Put/call", m_right);
    	p.add( "Multiplier", m_multiplier);
    	p.add( "Exchange", m_exchange);
    	p.add( "Comp. Exch.", m_compExch);
    	p.add( "Currency", m_currency);
    	p.add( "Local symbol", m_localSymbol);
    	p.add( "Trading class", m_tradingClass);
    	
    	//setLayout( new BorderLayout() );
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    	add( p);
        add(Box.createHorizontalStrut(20));
        add(fileChoosePanel);
        }
	
	@Override 
        public Dimension getMaximumSize() {
		return super.getPreferredSize();
	}
	
//	public void onOK() {
//		if (m_contract.isCombo() ) {
//			return;
//		}
//		
//		// component exchange is only relevant if exchange is SMART or BEST
//		String exch = m_exchange.getText().toUpperCase(); 
//		String compExch = exch.equals( "SMART") || exch.equals( "BEST") ? m_compExch.getText().toUpperCase() : null; 		
//		
//		m_contract.symbol( m_symbol.getText().toUpperCase() ); 
//		m_contract.secType( m_secType.getSelectedItem() ); 
//		m_contract.lastTradeDateOrContractMonth( m_lastTradeDateOrContractMonth.getText() ); 
//		m_contract.strike( m_strike.getDouble() ); 
//		m_contract.right( m_right.getSelectedItem() ); 
//		m_contract.multiplier( m_multiplier.getText() ); 
//		m_contract.exchange( exch);
//		m_contract.primaryExch( compExch);
//		m_contract.currency( m_currency.getText().toUpperCase() ); 
//		m_contract.localSymbol( m_localSymbol.getText().toUpperCase() );
//		m_contract.tradingClass( m_tradingClass.getText().toUpperCase() );
//	}
        
        //click requesting top market data will send request 
	public void onOK() {
		//if(contractList.isEmpty() || contractList == null)
                //    return;
		if(!contractList.isEmpty())
                    contractList.clear();
		// component exchange is only relevant if exchange is SMART or BEST
		String exch = m_exchange.getText().toUpperCase(); 
		String compExch = exch.equals( "SMART") || exch.equals( "BEST") ? m_compExch.getText().toUpperCase() : null; 		
		
                String[] symbols = m_symbol.getText().toUpperCase().split(",");
                
                for(String symbol : symbols){
                    Contract memberCon= new Contract();
                    memberCon.symbol(symbol);
                    memberCon.secType(m_secType.getSelectedItem());
                    memberCon.lastTradeDateOrContractMonth(m_lastTradeDateOrContractMonth.getText());
		    memberCon.strike( m_strike.getDouble() ); 
		    memberCon.right( m_right.getSelectedItem() ); 
		    memberCon.multiplier( m_multiplier.getText() ); 
		    memberCon.exchange( exch);
		    memberCon.primaryExch( compExch);
		    memberCon.currency( m_currency.getText().toUpperCase() ); 
		    memberCon.localSymbol( m_localSymbol.getText().toUpperCase() );
		    memberCon.tradingClass( m_tradingClass.getText().toUpperCase() );
                    contractList.add(memberCon);
                }
                if(symbols != null && symbols.length > 0 && !contractList.isEmpty() && contractList.size() > 0){
                   m_contract.symbol( symbols[0] ); 
                   m_contract.secType( m_secType.getSelectedItem() ); 
                   m_contract.lastTradeDateOrContractMonth( m_lastTradeDateOrContractMonth.getText() ); 
                   m_contract.strike( m_strike.getDouble() ); 
                   m_contract.right( m_right.getSelectedItem() ); 
                   m_contract.multiplier( m_multiplier.getText() ); 
                   m_contract.exchange( exch);
                   m_contract.primaryExch( compExch);
                   m_contract.currency( m_currency.getText().toUpperCase() ); 
                   m_contract.localSymbol( m_localSymbol.getText().toUpperCase() );
                   m_contract.tradingClass( m_tradingClass.getText().toUpperCase() );
                } 				
	}
        
        public void onReqDFOK(){
            String exch = m_exchange.getText().toUpperCase(); 
	    String compExch = exch.equals( "SMART") || exch.equals( "BEST") ? m_compExch.getText().toUpperCase() : null; 	
            
            ArrayList<String> symbols = this.fileChoosePanel.getSymbols();
            ArrayList<Contract> fileContracts = new ArrayList<Contract>();
            if(symbols != null && !symbols.isEmpty()){
                for(int i = 0; i < symbols.size(); i++){
                   Contract fc = new Contract();
                   fc.symbol(symbols.get(i));
                   fc.secType(m_secType.getSelectedItem());
                   fc.lastTradeDateOrContractMonth(m_lastTradeDateOrContractMonth.getText());
                   fc.strike(m_strike.getDouble());
                   fc.right(m_right.getSelectedItem());
                   fc.multiplier(m_multiplier.getText());
                   fc.exchange(exch);
                   fc.primaryExch(compExch);
                   fc.currency(m_currency.getText().toUpperCase());
                   fc.localSymbol(m_localSymbol.getText().toUpperCase());
                   fc.tradingClass(m_tradingClass.getText().toUpperCase());
                   fileContracts.add(fc);
                }
                fileChoosePanel.setFileContracts(fileContracts);
            }
            return;           
        }
        
        public ArrayList<Contract> getFileContracts(){
            return fileChoosePanel.getFileContracts();
        }
        public String getSecType(){
            return this.getSecType();
        }
        
        public String getLastTradeDateOrContractMonth(){
            return this.getLastTradeDateOrContractMonth();
        }

}
