package it.airdesk.airdesk_app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.airdesk.airdesk_app.model.Facility;
import it.airdesk.airdesk_app.repository.FacilityRepository;

@Service
public class FacilityService {
        
    @Autowired
    private FacilityRepository facilityRepository;

    public List<Facility> findAllByCity(String city) {
        return facilityRepository.findByAddress_CityContainingIgnoreCase(city);
    }

    public Map<Long, Integer> findWorkstationCountByFacility() {
        List<Object[]> results = facilityRepository.findWorkstationCountByFacility();
        Map<Long, Integer> facility2workstations = new HashMap<>();
        for (Object[] result : results) {
            Long facilityId = (Long) result[0];
            Integer workstations = ((Long) result[1]).intValue();
            facility2workstations.put(facilityId, workstations);
        }
        return facility2workstations;
    }

}
