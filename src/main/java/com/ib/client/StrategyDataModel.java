/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.client;

import apidemo.ApiDemo;
//import apidemo.TopModel;
import java.util.ArrayList;

/**
 *
 * @author jyang
 */
public class StrategyDataModel {
    private StrategyHisDataRow hisRow;
    private ArrayList<StrategyMktDataRow> streamMktRow;
    private Contract contract;
    
    public StrategyDataModel(StrategyHisDataRow hisRow, ArrayList<StrategyMktDataRow> streamMktRow, Contract contracct){
       this.hisRow = hisRow;
       this.streamMktRow = streamMktRow;
       this.contract = contracct;     
    }
    
    public void addMktDataRow(StrategyMktDataRow row){
            if(row == null || row.symbol.isEmpty())
                return;             
            streamMktRow.add(row);               
            //ApiDemo.INSTANCE.controller().reqTopMktData(contracts.get(i), "", false, row);                          
        }
    
    public void addMktDataRows(ArrayList<StrategyMktDataRow> rows){
    
    }

    
}
