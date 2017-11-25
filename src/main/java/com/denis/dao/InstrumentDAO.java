package com.denis.dao;

import java.util.List;

import com.denis.dto.ExceptionDto;
import com.denis.dto.Instrument;

public interface InstrumentDAO {

	List<Instrument> loadAllInstrument() throws ExceptionDto;

	Instrument getById(int id);

	boolean add(Instrument instrument);

	boolean update(Instrument instrument);

}
