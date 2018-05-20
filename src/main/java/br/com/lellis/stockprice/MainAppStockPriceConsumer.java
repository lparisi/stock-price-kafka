package br.com.lellis.stockprice;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.com.lellis.stockprice.consumer.StockPriceConsumer;

public class MainAppStockPriceConsumer {

	public static void main(String[] args) throws Exception {
		int numConsumers = 2;
		ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

		final var consumers = new ArrayList<StockPriceConsumer>(numConsumers);
		for (int i = 0; i < numConsumers; i++) {
			StockPriceConsumer consumer = new StockPriceConsumer(i);
			consumers.add(consumer);
			executor.submit(consumer);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (StockPriceConsumer consumer : consumers) {
					consumer.shutdown();
				}
				executor.shutdown();
				try {
					executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
