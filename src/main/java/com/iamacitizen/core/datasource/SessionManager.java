package com.iamacitizen.core.datasource;

import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.exception.SerigyDataSourceException;

import java.util.concurrent.ConcurrentHashMap;

public final class SessionManager {

	private static ConcurrentHashMap<Long, Session> sessionMap = new ConcurrentHashMap(32);

	/**
	 * Cria uma nova sessão, adicionando-a à lista de sessões ativas.
	 *
	 * @return
	 * @throws SerigyDataSourceException
	 */
	public static Session createSession() throws SerigyDataSourceException {
		/* Recupera o id da thread para atribuir como id da sessão */
		long id = Thread.currentThread().getId();

		/* Cria a sessão */
		Session session = new ActiveSession(id);
		sessionMap.put(id, session);

		return session;
	}

	/**
	 * Recupera uma sessão a partir de seu identificador.
	 *
	 * @param id
	 * @return ActiveSession
	 * @throws SerigyDataSourceException caso não exista uma sessão ativa.
	 */
	private static Session getSession(long id) throws SerigyDataSourceException {
		Session result = sessionMap.get(id);

		if (result == null) {
			throw new SerigyDataSourceException(SerigyConstant.DS_NO_SESSION);
		}

		return result;
	}

	/**
	 * Recupera a sessão ativa
	 *
	 * @return sessão
	 * @throws SerigyDataSourceException caso não exista uma sessão ativa.
	 */
	public static Session getActiveSession() throws SerigyDataSourceException {
		/* Recupera o id da thread */
		long id = Thread.currentThread().getId();

		return getSession(id);
	}

	/**
	 * Limpa a sessão, fechando a conexão com o banco de dados e removendo-a da lista de sessões ativas.
	 *
	 * @param session
	 */
	public static void cleanUp(Session session) {
		if (session != null) {
			((ActiveSession) session).close();
			sessionMap.remove(((ActiveSession) session).getId());
		}
	}

	/**
	 * Desfaz a transação da sessão ativa.
	 *
	 * @throws SerigyDataSourceException caso não exista uma sessão ativa.
	 */
	public static void rollbackSession() throws SerigyDataSourceException {
		((ActiveSession) getActiveSession()).rollback();
	}
}
