package com.sap.core.odata.ref.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.ref.model.Building;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.model.Employee;
import com.sap.core.odata.ref.model.Manager;
import com.sap.core.odata.ref.model.Photo;
import com.sap.core.odata.ref.model.Room;
import com.sap.core.odata.ref.model.Team;

/**
 * Data for the reference scenario
 * @author SAP AG
 */
public class ScenarioDataSource implements ListsDataSource {

  private static final Logger log = LoggerFactory.getLogger(ScenarioDataSource.class);

  private final DataContainer dataContainer;

  public ScenarioDataSource(final DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }

  @Override
  public List<?> readData(final EdmEntitySet entitySet) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    if ("Employees".equals(entitySet.getName()))
      data.addAll(dataContainer.getEmployeeSet());
    else if ("Teams".equals(entitySet.getName()))
      data.addAll(dataContainer.getTeamSet());
    else if ("Rooms".equals(entitySet.getName()))
      data.addAll(dataContainer.getRoomSet());
    else if ("Managers".equals(entitySet.getName()))
      data.addAll(dataContainer.getManagerSet());
    else if ("Buildings".equals(entitySet.getName()))
      data.addAll(dataContainer.getBuildingSet());
    else if ("Photos".equals(entitySet.getName()))
      data.addAll(dataContainer.getPhotoSet());
    else
      throw new ODataNotImplementedException();
    return data;
  }

  @Override
  public Object readData(final EdmEntitySet entitySet, final Map<String, Object> keys) throws ODataException {
    if ("Employees".equals(entitySet.getName())) {
      for (Employee employee : dataContainer.getEmployeeSet())
        if (employee.getId().equals(keys.get("EmployeeId")))
          return employee;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Teams".equals(entitySet.getName())) {
      for (Team team : dataContainer.getTeamSet())
        if (team.getId().equals(keys.get("Id")))
          return team;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Rooms".equals(entitySet.getName())) {
      for (Room room : dataContainer.getRoomSet())
        if (room.getId().equals(keys.get("Id")))
          return room;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Managers".equals(entitySet.getName())) {
      for (Manager manager : dataContainer.getManagerSet())
        if (manager.getId().equals(keys.get("EmployeeId")))
          return manager;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Buildings".equals(entitySet.getName())) {
      for (Building building : dataContainer.getBuildingSet())
        if (building.getId().equals(keys.get("Id")))
          return building;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    } else if ("Photos".equals(entitySet.getName())) {
      for (Photo photo : dataContainer.getPhotoSet())
        if (photo.getId() == (Integer) keys.get("Id") && photo.getType().equals(keys.get("Type")))
          return photo;
      throw new ODataNotFoundException(ODataNotFoundException.USER);
    }

    throw new ODataNotImplementedException();
  }

  @Override
  public Object newDataObject(final EdmEntitySet entitySet) throws ODataException {
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

  @Override
  public Object readRelatedData(final EdmEntitySet sourceEntitySet, final Object sourceData, final EdmEntitySet targetEntitySet, final Map<String, Object> targetKeys) throws ODataException {
    return null;
  }
}
