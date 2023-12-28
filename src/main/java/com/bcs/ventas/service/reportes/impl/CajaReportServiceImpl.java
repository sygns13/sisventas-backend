package com.bcs.ventas.service.reportes.impl;

import com.bcs.ventas.dao.CobroVentaDAO;
import com.bcs.ventas.dao.IngresoSalidaCajaDAO;
import com.bcs.ventas.dao.PagoProveedorDAO;
import com.bcs.ventas.dao.mappers.CobroVentaMapper;
import com.bcs.ventas.dao.mappers.IngresoSalidaCajaMapper;
import com.bcs.ventas.dao.mappers.PagoProveedorMapper;
import com.bcs.ventas.model.dto.CajaSucursalDTO;
import com.bcs.ventas.model.entities.Almacen;
import com.bcs.ventas.model.entities.CobroVenta;
import com.bcs.ventas.model.entities.IngresoSalidaCaja;
import com.bcs.ventas.model.entities.PagoProveedor;
import com.bcs.ventas.service.AlmacenService;
import com.bcs.ventas.service.reportes.CajaReportService;
import com.bcs.ventas.utils.Constantes;
import com.bcs.ventas.utils.beans.ClaimsAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class CajaReportServiceImpl implements CajaReportService {

    @Autowired
    private ClaimsAuthorization claimsAuthorization;

    @Autowired
    private CobroVentaDAO cobroVentaDAO;

    @Autowired
    private CobroVentaMapper cobroVentaMapper;

    @Autowired
    private PagoProveedorDAO pagoProveedorDAO;

    @Autowired
    private PagoProveedorMapper pagoProveedorMapper;

    @Autowired
    private IngresoSalidaCajaDAO ingresoSalidaCajaDAO;

    @Autowired
    private IngresoSalidaCajaMapper ingresoSalidaCajaMapper;

    @Autowired
    private AlmacenService almacenService;
    @Override
    public CajaSucursalDTO cajaDiariaSucursalReporte(String fecha, Long almacenID) throws Exception {

        //Oauth inicio
        Long EmpresaId = claimsAuthorization.getEmpresaId();
        //Oauth final
        CajaSucursalDTO cajaSucursalDTO = new CajaSucursalDTO();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NO_BORRADO", Constantes.REGISTRO_BORRADO);
        params.put("EMPRESA_ID",EmpresaId);
        params.put("FECHA",fecha);

        if(almacenID != null && almacenID.compareTo(Constantes.CANTIDAD_ZERO_LONG) > 0){
            params.put("ALMACEN_ID",almacenID);
            Almacen almacen = almacenService.listarPorId(almacenID);
            cajaSucursalDTO.setAlmacen(almacen);
        }

        List<CobroVenta> cobroVentas = cobroVentaMapper.listByParameterMap(params);
        List<PagoProveedor> pagoProveedors = pagoProveedorMapper.listByParameterMap(params);

        params.put("TIPO",Constantes.INGRESO_SALIDAS_CAJA_TIPO_INGRESO);
        List<IngresoSalidaCaja> ingresoOtros = ingresoSalidaCajaMapper.listByParameterMap(params);

        params.put("TIPO",Constantes.INGRESO_SALIDAS_CAJA_TIPO_SALIDA);
        List<IngresoSalidaCaja> salidaOtros = ingresoSalidaCajaMapper.listByParameterMap(params);


        cajaSucursalDTO.setIngresosVentas(new BigDecimal("0"));
        cajaSucursalDTO.setEgresosCompras(new BigDecimal("0"));
        cajaSucursalDTO.setIngresosOtros(new BigDecimal("0"));
        cajaSucursalDTO.setEgresosOtros(new BigDecimal("0"));

        cajaSucursalDTO.setCajaInicial(new BigDecimal("0"));

        cobroVentas.forEach(cobroVenta -> {
            cajaSucursalDTO.setIngresosVentas(cajaSucursalDTO.getIngresosVentas().add(cobroVenta.getImporte()));
        });

        pagoProveedors.forEach(pagoProveedor -> {
            cajaSucursalDTO.setEgresosCompras(cajaSucursalDTO.getEgresosCompras().add(pagoProveedor.getMontoReal()));
        });

        ingresoOtros.forEach(ingresoOtro -> {
            cajaSucursalDTO.setIngresosOtros(cajaSucursalDTO.getIngresosOtros().add(ingresoOtro.getMonto()));
        });

        salidaOtros.forEach(salidaOtro -> {
            cajaSucursalDTO.setEgresosOtros(cajaSucursalDTO.getEgresosOtros().add(salidaOtro.getMonto()));
        });

        cajaSucursalDTO.setIngresosTotal(cajaSucursalDTO.getIngresosVentas().add(cajaSucursalDTO.getIngresosOtros()));
        cajaSucursalDTO.setEgresosTotal(cajaSucursalDTO.getEgresosCompras().add(cajaSucursalDTO.getEgresosOtros()));

        cajaSucursalDTO.setCajaTotal(cajaSucursalDTO.getIngresosTotal().subtract(cajaSucursalDTO.getEgresosTotal()));

        return cajaSucursalDTO;
    }
}
