package com.denis.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.denis.dao.CatalogInstrumentDAO;
import com.denis.dto.CatalogInstrument;
import com.denis.dto.ExceptionDto;

@Repository("catalogInstrumentDAO")
@Transactional
public class CatalogInstrumentDAOImpl implements CatalogInstrumentDAO {

	private final String EXCEPTION_PREFIX = "Problem in CatalogInstrumentDAOImpl in";

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public List<CatalogInstrument> loadAllCategories() throws ExceptionDto {
		return sessionFactory.getCurrentSession().createQuery("FROM CatalogInstrument", CatalogInstrument.class)
				.getResultList();
	}

	@Override
	public CatalogInstrument getById(int id) {
		try {
			return sessionFactory.getCurrentSession().get(CatalogInstrument.class, Integer.valueOf(id));
		} catch (Exception ex) {
			throw new ExceptionDAO(EXCEPTION_PREFIX + " getById method.", ex);
		}
	}

	@Override
	public boolean add(CatalogInstrument catalogInstrument) {

		try {
			sessionFactory.getCurrentSession().persist(catalogInstrument);

			return true;
		} catch (Exception ex) {
			throw new ExceptionDAO(EXCEPTION_PREFIX + " add method.", ex);
		}

	}

	@Override
	public boolean update(CatalogInstrument catalogInstrument) {
		try {
			sessionFactory.getCurrentSession().update(catalogInstrument);
			return true;
		}

		catch (Exception ex) {
			throw new ExceptionDAO(EXCEPTION_PREFIX + " update method.", ex);
		}
	}

}
