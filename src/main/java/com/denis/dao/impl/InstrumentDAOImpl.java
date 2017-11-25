package com.denis.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.denis.dao.InstrumentDAO;
import com.denis.dto.ExceptionDto;
import com.denis.dto.Instrument;

@Repository("instrumentDAO")
@Transactional
public class InstrumentDAOImpl implements InstrumentDAO {

	private final String EXCEPTION_PREFIX = "Problem in InstrumentDAOImpl in";

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public List<Instrument> loadAllInstrument() throws ExceptionDto {
		return sessionFactory.getCurrentSession().createQuery("FROM Instrument", Instrument.class).getResultList();
	}

	@Override
	public Instrument getById(int id) {
		try {
			return sessionFactory.getCurrentSession().get(Instrument.class, Integer.valueOf(id));
		} catch (Exception ex) {
			throw new ExceptionDAO(EXCEPTION_PREFIX + " getById method.", ex);
		}
	}

	@Override
	public boolean add(Instrument instrument) {

		try {
			sessionFactory.getCurrentSession().persist(instrument);

			return true;
		} catch (Exception ex) {
			throw new ExceptionDAO(EXCEPTION_PREFIX + " add method.", ex);
		}

	}

	@Override
	public boolean update(Instrument instrument) {
		try {
			sessionFactory.getCurrentSession().update(instrument);
			return true;
		}

		catch (Exception ex) {
			throw new ExceptionDAO(EXCEPTION_PREFIX + " update method.", ex);
		}
	}

}
