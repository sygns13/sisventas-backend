package com.bcs.ventas.service;

import com.bcs.ventas.model.Pet;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

public interface PetService {

    byte[] exportPdf(List<Pet> pets) throws JRException, FileNotFoundException;
    byte[] exportXls(List<Pet> pets) throws JRException, FileNotFoundException;
}
