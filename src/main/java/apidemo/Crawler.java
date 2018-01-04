package apidemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.NewsType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController;
import com.ib.controller.ApiController.IBulletinHandler;
import com.ib.controller.ApiController.IConnectionHandler;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.ITimeHandler;
import com.ib.controller.Bar;
import com.ib.controller.Formats;

import apidemo.util.IConnectionConfiguration.DefaultConnectionConfiguration;
import redis.clients.jedis.Jedis;

class Logger implements ILogger {
	private String title;

	Logger(String title) {
		this.title = title;
	}

	@Override
	public void log(final String str) {
		System.out.println(title + ": " + str);
	}
}

class RHandler implements IHistoricalDataHandler {
	private Contract contract;
	private static Jedis jedis = new Jedis("localhost");

	public RHandler(Contract contract) {
		this.contract = contract;
	}

	private void storeBar(Bar bar) {
		jedis.lpush(contract.symbol(), format(bar));
	}

	public static ArrayList<String> getBar(String symbol) {
		return (ArrayList<String>) jedis.lrange(symbol, 0, -1);
	}

	private String format(Bar bar) {
		return String.format("%s,%s,%s,%s,%s", Bar.format(bar.m_time), bar.m_open, bar.m_high, bar.m_low, bar.m_close);
	}

	@Override
	public void historicalData(Bar bar, boolean hasGaps) {
		System.out.println("Contract: " + contract.toString());
		System.out.println(bar.toString());
		this.storeBar(bar);

	}

	@Override
	public void historicalDataEnd() {
		// TODO Auto-generated method stub
		System.out.println("Query Finished!");
		String status = jedis.save();
		System.out.println(status);
	}

}

public class Crawler implements IConnectionHandler {
	private String filepath;
	private ApiController m_controller;
	private final Logger m_inLogger = new Logger("In");
	private final Logger m_outLogger = new Logger("Out");

	@Override
	public void connected() {
		show("connected");

		controller().reqCurrentTime(new ITimeHandler() {
			@Override
			public void currentTime(long time) {
				show("Server date/time is " + Formats.fmtDate(time * 1000));
			}
		});

		controller().reqBulletins(true, new IBulletinHandler() {
			@Override
			public void bulletin(int msgId, NewsType newsType, String message, String exchange) {
				String str = String.format("Received bulletin:  type=%s  exchange=%s", newsType, exchange);
				show(str);
				show(message);
			}
		});
	}

	@Override
	public void disconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void accountList(ArrayList<String> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(Exception e) {
		e.printStackTrace();

		// TODO Auto-generated method stub

	}

	@Override
	public void message(int id, int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		show(id + " " + errorCode + " " + errorMsg);
	}

	@Override
	public void show(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);

	}

	public Crawler(String filepath) {
		this.filepath = filepath;
		DefaultConnectionConfiguration config = new DefaultConnectionConfiguration();

		int port = Integer.parseInt(config.getDefaultPort());
		int clientId = 0;
		this.controller().connect(config.getDefaultHost(), port, clientId, "");
	}

	public void disconnect() {
		controller().disconnect();
	}

	private ApiController controller() {
		if (m_controller == null) {
			m_controller = new ApiController(this, m_inLogger, m_outLogger);
		}
		return m_controller;
	}

	public void getHisBySymbols(String datestr, int limit) {
		try {
			if (datestr == null || datestr.equals("")) {
				datestr = "20171103";
			}
			ArrayList<String> symbols = loadSymbols(this.filepath, limit);
			this.requestData(symbols, datestr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * Retrieve symbols from filepath
	 * 
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	private ArrayList<String> loadSymbols(String filepath, int limit) throws Exception {
		ArrayList<String> symbols = new ArrayList<String>();
		File file = new File(filepath);
		FileReader fr = new FileReader(file.getAbsolutePath());
		BufferedReader br = new BufferedReader(fr);
		String content = "";
		int i = 0;
		while ((content = br.readLine()) != null) {
			if (i == limit) {
				break;
			}

			symbols.add(content.trim().split(",")[1].toUpperCase());
			i++;
		}
		br.close();
		fr.close();
		return symbols;
	}

	private void requestData(ArrayList<String> symbols, String datestr) throws InterruptedException {
		ArrayList<Contract> fileContracts = new ArrayList<Contract>();
		if (symbols != null && !symbols.isEmpty()) {
			for (int i = 0; i < symbols.size(); i++) {
				Contract fc = new Contract();
				fc.symbol(symbols.get(i));
				fc.secType("STK");
				fc.lastTradeDateOrContractMonth("");
				fc.strike(0.0);
				fc.right("");
				fc.multiplier("");
				fc.exchange("SMART");
				fc.primaryExch("ISLAND");
				fc.currency("USD");
				fc.localSymbol("");
				fc.tradingClass("");
				fileContracts.add(fc);
			}
		}
		boolean rth = false;
		if (fileContracts != null && !fileContracts.isEmpty()) {
			for (int i = 0; i < fileContracts.size(); i++) {
				RHandler panel = new RHandler(fileContracts.get(i));
				this.controller().reqHistoricalData(fileContracts.get(i), datestr, days, DurationUnit.DAY,
						BarSize._1_day, WhatToShow.TRADES, rth, panel);
				Thread.sleep(2000);
			}
		}
		return;
	}

	static int days = 30;

	public static void main(String[] args) {
		int limit = -1;
		Crawler crawler = new Crawler("symbols.csv");
		crawler.getHisBySymbols("20180103 16:00:00", limit);

	}
}
