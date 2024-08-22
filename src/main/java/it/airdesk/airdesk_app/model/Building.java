package it.airdesk.airdesk_app.model;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.airdesk.airdesk_app.model.dataTypes.Address;
import it.airdesk.airdesk_app.model.dataTypes.OfficeHours;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Building {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "address field must not be null")
    @Column(nullable = false)
    @Embedded
    private Address address;

    @NotNull(message = "facility field must not be null")
    @Column(nullable = false)
    private Facility facility;

    @NotNull(message = "opening hours must not be null")
    @Column(nullable = false)
    private Map<DayOfWeek, List<OfficeHours>> openingHours = new HashMap<>(); //value is a list in order to handle duplicate opening hours if a building closes at lunch and reopens at noon
    
    @NotNull(message = "facility field must not be null")
    @Column(nullable = false)
    private List<Floor> floors = new ArrayList<>();

    public Building(){}

    /////////////       GETTERS+SETTERS       //////////////////////
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Map<DayOfWeek, List<OfficeHours>> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<DayOfWeek, List<OfficeHours>> openingTimes) {
        this.openingHours = openingHours;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }


    /////////////       AUXILIARY METHODS       ////////////////////
    public void updateOpeningHours(DayOfWeek day, List<OfficeHours> officeHours) {
        this.openingHours.put(day, officeHours);
    }

    public void addFloor(Floor floor) {
        this.floors.add(floor);
    }

    /////////////       HashCode + equals METHODS       ////////////

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Building other = (Building) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        return true;
    }


    
}
