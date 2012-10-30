package com.sap.core.odata.ref.processor;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.ref.model.Building;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.model.Employee;
import com.sap.core.odata.ref.model.Manager;
import com.sap.core.odata.ref.model.Photo;
import com.sap.core.odata.ref.model.Room;
import com.sap.core.odata.ref.model.Team;

public class ScenarioDataSource implements CollectionsDataSource {

  private static final Logger log = LoggerFactory.getLogger(ScenarioDataSource.class);

  private final DataContainer dataContainer;

  public ScenarioDataSource(final DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }

  @Override
  public Collection<?> readDataSet(EdmEntitySet entitySet) throws ODataError {
    if ("Employees".equals(entitySet.getName()))
      return dataContainer.getEmployeeSet();
    else if ("Teams".equals(entitySet.getName()))
      return dataContainer.getTeamSet();
    else if ("Rooms".equals(entitySet.getName()))
      return dataContainer.getRoomSet();
    else if ("Managers".equals(entitySet.getName()))
      return dataContainer.getManagerSet();
    else if ("Buildings".equals(entitySet.getName()))
      return dataContainer.getBuildingSet();
    else if ("Photos".equals(entitySet.getName()))
      return dataContainer.getPhotoSet();
    else
      throw new ODataNotImplementedException();
  }

  @Override
  public Object readDataObject(EdmEntitySet entitySet, List<KeyPredicate> keys) throws ODataError {
    if ("Employees".equals(entitySet.getName())) {
      for (Employee employee : dataContainer.getEmployeeSet())
        if (employee.getId().equals(keys.get(0).getLiteral()))
          return employee;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Teams".equals(entitySet.getName())) {
      for (Team team : dataContainer.getTeamSet())
        if (team.getId().equals(keys.get(0).getLiteral()))
          return team;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Rooms".equals(entitySet.getName())) {
      for (Room room : dataContainer.getRoomSet())
        if (room.getId().equals(keys.get(0).getLiteral()))
          return room;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Managers".equals(entitySet.getName())) {
      for (Manager manager : dataContainer.getManagerSet())
        if (manager.getId().equals(keys.get(0).getLiteral()))
          return manager;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Buildings".equals(entitySet.getName())) {
      for (Building building : dataContainer.getBuildingSet())
        if (building.getId().equals(keys.get(0).getLiteral()))
          return building;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Photos".equals(entitySet.getName())) {
      for (Photo photo : dataContainer.getPhotoSet())
        if (photo.getId() == Integer.parseInt(keys.get(0).getLiteral()) && photo.getType().equals(keys.get(1).getLiteral()))
          return photo;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    }

    throw new ODataNotImplementedException();
  }

  @Override
  public Object newDataObject(EdmEntitySet entitySet) throws ODataError {
    if ("Employees".equals(entitySet.getName()))
      return new Employee();
    else if ("Teams".equals(entitySet.getName()))
      return new Team();
    else if ("Rooms".equals(entitySet.getName()))
      return new Room();
    else if ("Managers".equals(entitySet.getName()))
      return new Manager();
    else if ("Buildings".equals(entitySet.getName()))
      return new Building();
    else if ("Photos".equals(entitySet.getName()))
      return new Photo();
    else
      throw new ODataNotImplementedException();
  }

}
