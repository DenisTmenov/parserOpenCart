package com.denis.dao;

import java.util.List;

import com.denis.dto.CatalogInstrument;
import com.denis.dto.ExceptionDto;

public interface CatalogInstrumentDAO {

	List<CatalogInstrument> loadAllCategories() throws ExceptionDto;

	CatalogInstrument getById(int id);

	boolean add(CatalogInstrument catalogInstrument);

	boolean update(CatalogInstrument catalogInstrument);

}
