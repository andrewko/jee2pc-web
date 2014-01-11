package sk.garwan.jee2pc.service;

import javax.ejb.Local;

@Local
public interface TransactionServiceJmsDb {

	void doInTransaction();

	void doInTransactionWithException();

	void doInTransactionWithException2();

	void doWithoutTransactionWithException();

	void doWithoutTransactionWithException2();
}
