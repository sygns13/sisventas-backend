package com.bcs.ventas.service.impl;

import com.bcs.ventas.model.Pet;
import com.bcs.ventas.service.PetService;
import com.bcs.ventas.service.jasper.ClienteJasperService;
import com.bcs.ventas.service.jasper.PetReportGenerator;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class PetServiceImpl implements PetService {



    @Autowired
    private PetReportGenerator petReportGenerator;

    @Autowired
    private ClienteJasperService clienteJasperService;



    @Override
    public byte[] exportPdf(List<Pet> pets) throws JRException, FileNotFoundException {
        return petReportGenerator.exportToPdf(pets);
        //return clienteJasperService.exportToPdf(pets,  "petsData","testReport");
    }

    @Override
    public byte[] exportXls(List<Pet> pets) throws JRException, FileNotFoundException {
        return petReportGenerator.exportToXls(pets);
        //return clienteJasperService.exportToXls(pets, "petsData", "testReport", "hoja1");
    }
}
