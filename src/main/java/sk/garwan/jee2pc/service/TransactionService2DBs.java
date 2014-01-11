package sk.garwan.jee2pc.service;

import javax.ejb.Local;

@Local
public interface TransactionService2DBs {

	void doInTransaction();

	void doInTransactionWithException();

	void doWithoutTransactionWithException();
}
