package com.bcs.ventas.controller.reportes;

import com.bcs.ventas.model.Pet;
import com.bcs.ventas.service.PetService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf() throws JRException, FileNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("petsReport", "petsReport.pdf");

        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Luna");
        pet1.setBirthday(new Date());
        pet1.setBreed("Pitbull");

        Pet pet2 = new Pet();
        pet2.setId(22L);
        pet2.setName("Kale");
        pet2.setBirthday(new Date());
        pet2.setBreed("Labrador");

        List<Pet> pets = List.of(pet1, pet2);


        return ResponseEntity.ok().headers(headers).body(petService.exportPdf(pets));
    }

    @GetMapping("/export-xls")
    public ResponseEntity<byte[]> exportXls() throws JRException, FileNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
        var contentDisposition = ContentDisposition.builder("attachment")
                .filename("petsReport" + ".xlsx").build();
        headers.setContentDisposition(contentDisposition);

        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Luna");
        pet1.setBirthday(new Date());
        pet1.setBreed("Pitbull");

        Pet pet2 = new Pet();
        pet2.setId(22L);
        pet2.setName("Kale");
        pet2.setBirthday(new Date());
        pet2.setBreed("Labrador");

        List<Pet> pets = List.of(pet1, pet2);

        return ResponseEntity.ok()
                .headers(headers)
                .body(petService.exportXls(pets));
    }
}
