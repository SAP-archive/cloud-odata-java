package com.sap.core.odata.ref.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.ref.model.Building;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.model.Employee;
import com.sap.core.odata.ref.model.Location;
import com.sap.core.odata.ref.model.Manager;
import com.sap.core.odata.ref.model.Photo;
import com.sap.core.odata.ref.model.Room;
import com.sap.core.odata.ref.model.Team;

/**
 * Data for the reference scenario
 * @author SAP AG
 */
public class ScenarioDataSource implements ListsDataSource {

  private final DataContainer dataContainer;

  public ScenarioDataSource(final DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }

  @Override
  public List<?> readData(final EdmEntitySet entitySet) throws ODataNotImplementedException, ODataNotFoundException, EdmException {
    if ("Employees".equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getEmployeeSet().toArray());
    else if ("Teams".equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getTeamSet().toArray());
    else if ("Rooms".equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getRoomSet().toArray());
    else if ("Managers".equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getManagerSet().toArray());
    else if ("Buildings".equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getBuildingSet().toArray());
    else if ("Photos".equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getPhotoSet().toArray());
    else
      throw new ODataNotImplementedException();
  }

  @Override
  public Object readData(final EdmEntitySet entitySet, final Map<String, Object> keys) throws ODataNotImplementedException, ODataNotFoundException, EdmException {
    if ("Employees".equals(entitySet.getName())) {
      for (final Employee employee : dataContainer.getEmployeeSet())
        if (employee.getId().equals(keys.get("EmployeeId")))
          return employee;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if ("Teams".equals(entitySet.getName())) {
      for (final Team team : dataContainer.getTeamSet())
        if (team.getId().equals(keys.get("Id")))
          return team;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if ("Rooms".equals(entitySet.getName())) {
      for (final Room room : dataContainer.getRoomSet())
        if (room.getId().equals(keys.get("Id")))
          return room;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if ("Managers".equals(entitySet.getName())) {
      for (final Manager manager : dataContainer.getManagerSet())
        if (manager.getId().equals(keys.get("EmployeeId")))
          return manager;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if ("Buildings".equals(entitySet.getName())) {
      for (final Building building : dataContainer.getBuildingSet())
        if (building.getId().equals(keys.get("Id")))
          return building;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if ("Photos".equals(entitySet.getName())) {
      for (final Photo photo : dataContainer.getPhotoSet())
        if (photo.getId() == (Integer) keys.get("Id")
            && photo.getType().equals(keys.get("Type")))
          return photo;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    throw new ODataNotImplementedException();
  }

  @Override
  public Object readRelatedData(final EdmEntitySet sourceEntitySet, final Object sourceData, final EdmEntitySet targetEntitySet, final Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException {
    if ("Employees".equals(targetEntitySet.getName())) {
      ArrayList<Object> data = new ArrayList<Object>();
      if ("Teams".equals(sourceEntitySet.getName()))
        data.addAll(((Team) sourceData).getEmployees());
      else if ("Rooms".equals(sourceEntitySet.getName()))
        data.addAll(((Room) sourceData).getEmployees());
      else if ("Managers".equals(sourceEntitySet.getName()))
        data.addAll(((Manager) sourceData).getEmployees());

      if (data.isEmpty())
        throw new ODataNotFoundException(null);
      if (targetKeys.isEmpty())
        return data;
      else
        for (final Object employee : data)
          if (((Employee) employee).getId().equals(targetKeys.get("EmployeeId")))
            return employee;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if ("Teams".equals(targetEntitySet.getName())) {
      if (((Employee) sourceData).getTeam() == null)
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      else
        return ((Employee) sourceData).getTeam();

    } else if ("Rooms".equals(targetEntitySet.getName())) {
      if ("Employees".equals(sourceEntitySet.getName())) {
        if (((Employee) sourceData).getRoom() == null)
          throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
        else
          return ((Employee) sourceData).getRoom();
      } else if ("Buildings".equals(sourceEntitySet.getName())) {
        List<Room> data = ((Building) sourceData).getRooms();
        if (data.isEmpty())
          throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
        if (targetKeys.isEmpty())
          return data;
        else
          for (final Object room : data)
            if (((Room) room).getId().equals(targetKeys.get("Id")))
              return room;
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }
      throw new ODataNotImplementedException();

    } else if ("Managers".equals(targetEntitySet.getName())) {
      if (((Employee) sourceData).getManager() == null)
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      else
        return ((Employee) sourceData).getManager();

    } else if ("Buildings".equals(targetEntitySet.getName())) {
      if (((Room) sourceData).getBuilding() == null)
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      else
        return ((Room) sourceData).getBuilding();

    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public Object readData(final EdmFunctionImport function, final Map<String, Object> parameters, final Map<String, Object> keys) throws ODataNotImplementedException, ODataNotFoundException, EdmException {
    if (function.getName().equals("EmployeeSearch")) {
      if (parameters.get("q") == null) {
        throw new ODataNotFoundException(null);
      } else {
        final List<Employee> found = searchEmployees((String) parameters.get("q"));
        if (keys.isEmpty())
          return found;
        else
          for (final Employee employee : found)
            if (employee.getId().equals(keys.get("EmployeeId")))
              return employee;
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    } else if (function.getName().equals("AllLocations")) {
      return Arrays.asList(getLocations().keySet().toArray());

    } else if (function.getName().equals("AllUsedRoomIds")) {
      ArrayList<String> data = new ArrayList<String>();
      for (final Room room : dataContainer.getRoomSet())
        if (!room.getEmployees().isEmpty())
          data.add(room.getId());
      if (data.isEmpty())
        throw new ODataNotFoundException(null);
      else
        return data;

    } else if (function.getName().equals("MaximalAge")) {
      return getOldestEmployee().getAge();

    } else if (function.getName().equals("MostCommonLocation")) {
      return getMostCommonLocation();

    } else if (function.getName().equals("ManagerPhoto")) {
      if (parameters.get("Id") == null)
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      for (final Manager manager : dataContainer.getManagerSet())
        if (manager.getId().equals(parameters.get("Id")))
          return manager.getImage();
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if (function.getName().equals("OldestEmployee")) {
      return getOldestEmployee();

    } else {
      throw new ODataNotImplementedException();
    }
  }

  private List<Employee> searchEmployees(final String search) {
    List<Employee> employees = new ArrayList<Employee>();
    for (final Employee employee : dataContainer.getEmployeeSet())
      if (employee.getEmployeeName().contains(search)
          || employee.getLocation() != null
          && (employee.getLocation().getCity().getCityName().contains(search)
              || employee.getLocation().getCity().getPostalCode().contains(search)
              || employee.getLocation().getCountry().contains(search)))
        employees.add(employee);
    return employees;
  }

  private HashMap<Location, Integer> getLocations() throws ODataNotFoundException {
    HashMap<Location, Integer> locations = new HashMap<Location, Integer>();
    for (Employee employee : dataContainer.getEmployeeSet())
      if (employee.getLocation() != null && employee.getLocation().getCity() != null) {
        boolean found = false;
        for (final Location location : locations.keySet())
          if (employee.getLocation().getCity().getPostalCode() == location.getCity().getPostalCode()
              && employee.getLocation().getCity().getCityName() == location.getCity().getCityName()
              && employee.getLocation().getCountry() == location.getCountry()) {
            found = true;
            locations.put(location, locations.get(location) + 1);
          }
        if (!found)
          locations.put(employee.getLocation(), 1);
      }
    if (locations.isEmpty())
      throw new ODataNotFoundException(null);
    else
      return locations;
  }

  private Location getMostCommonLocation() throws ODataNotFoundException {
    Integer count = 0;
    Location location = null;
    for (Entry<Location, Integer> entry : getLocations().entrySet())
      if (entry.getValue() > count) {
        count = entry.getValue();
        location = entry.getKey();
      }
    return location;
  }

  private Employee getOldestEmployee() {
    Employee oldestEmployee = null;
    for (final Employee employee : dataContainer.getEmployeeSet())
      if (oldestEmployee == null || employee.getAge() > oldestEmployee.getAge())
        oldestEmployee = employee;
    return oldestEmployee;
  }

  @Override
  public byte[] readBinaryData(final EdmEntitySet entitySet, final Object mediaLinkEntryData, StringBuilder mimeType) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    if (mediaLinkEntryData == null)
      throw new ODataNotFoundException(null);

    if ("Employees".equals(entitySet.getName()) || "Managers".equals(entitySet.getName())) {
      final Employee employee = (Employee) mediaLinkEntryData;
      mimeType.append(employee.getImageType());
      return employee.getImage();
    } else if ("Photos".equals(entitySet.getName())) {
      final Photo photo = (Photo) mediaLinkEntryData;
      mimeType.append(photo.getType());
      return photo.getImage();
    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public Object newDataObject(final EdmEntitySet entitySet) throws ODataNotImplementedException, EdmException {
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
  public void deleteData(final EdmEntitySet entitySet, final Map<String, Object> keys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    final Object data = readData(entitySet, keys);

    if ("Employees".equals(entitySet.getName())) {
      if (data instanceof Manager)
        for (Employee employee : ((Manager) data).getEmployees())
          employee.setManager(null);
      ((Employee) data).getTeam().getEmployees().remove(data);
      ((Employee) data).getRoom().getEmployees().remove(data);
      ((Employee) data).getManager().getEmployees().remove(data);
      dataContainer.getEmployeeSet().remove(data);

    } else if ("Teams".equals(entitySet.getName())) {
      for (Employee employee : ((Team) data).getEmployees())
        employee.setTeam(null);
      dataContainer.getTeamSet().remove(data);

    } else if ("Rooms".equals(entitySet.getName())) {
      for (Employee employee : ((Room) data).getEmployees())
        employee.setRoom(null);
      ((Room) data).getBuilding().getRooms().remove(data);
      dataContainer.getRoomSet().remove(data);

    } else if ("Managers".equals(entitySet.getName())) {
      for (Employee employee : ((Manager) data).getEmployees())
        employee.setManager(null);
      ((Manager) data).getTeam().getEmployees().remove(data);
      ((Manager) data).getRoom().getEmployees().remove(data);
      ((Manager) data).getManager().getEmployees().remove(data);
      dataContainer.getManagerSet().remove(data);

    } else if ("Buildings".equals(entitySet.getName())) {
      for (Room room : ((Building) data).getRooms())
        room.setBuilding(null);
      dataContainer.getBuildingSet().remove(data);

    } else if ("Photos".equals(entitySet.getName())) {
      dataContainer.getPhotoSet().remove(data);

    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public void deleteRelation(final EdmEntitySet sourceEntitySet, Object sourceData, final EdmEntitySet targetEntitySet, final Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    if ("Employees".equals(targetEntitySet.getName())) {
      if ("Teams".equals(sourceEntitySet.getName())) {
        for (Iterator<Employee> iterator = ((Team) sourceData).getEmployees().iterator(); iterator.hasNext();) {
          final Employee employee = iterator.next();
          if (employee.getId().equals(targetKeys.get("EmployeeId"))) {
            employee.setTeam(null);
            iterator.remove();
          }
        }
      } else if ("Rooms".equals(sourceEntitySet.getName())) {
        for (Iterator<Employee> iterator = ((Room) sourceData).getEmployees().iterator(); iterator.hasNext();) {
          final Employee employee = iterator.next();
          if (employee.getId().equals(targetKeys.get("EmployeeId"))) {
            employee.setRoom(null);
            iterator.remove();
          }
        }
      } else if ("Managers".equals(sourceEntitySet.getName())) {
        for (Iterator<Employee> iterator = ((Manager) sourceData).getEmployees().iterator(); iterator.hasNext();) {
          final Employee employee = iterator.next();
          if (employee.getId().equals(targetKeys.get("EmployeeId"))) {
            employee.setManager(null);
            iterator.remove();
          }
        }
      }

    } else if ("Teams".equals(targetEntitySet.getName())) {
      ((Employee) sourceData).getTeam().getEmployees().remove(sourceData);
      ((Employee) sourceData).setTeam(null);

    } else if ("Rooms".equals(targetEntitySet.getName())) {
      if ("Employees".equals(sourceEntitySet.getName())) {
        ((Employee) sourceData).getRoom().getEmployees().remove(sourceData);
        ((Employee) sourceData).setRoom(null);
      } else if ("Buildings".equals(sourceEntitySet.getName())) {
        for (Iterator<Room> iterator = ((Building) sourceData).getRooms().iterator(); iterator.hasNext();) {
          final Room room = iterator.next();
          if (room.getId().equals(targetKeys.get("Id"))) {
            room.setBuilding(null);
            iterator.remove();
          }
        }
      }

    } else if ("Managers".equals(targetEntitySet.getName())) {
      ((Employee) sourceData).getManager().getEmployees().remove(sourceData);
      ((Employee) sourceData).setManager(null);

    } else if ("Buildings".equals(targetEntitySet.getName())) {
      ((Room) sourceData).getBuilding().getRooms().remove(sourceData);
      ((Room) sourceData).setBuilding(null);

    } else {
      throw new ODataNotImplementedException();
    }
  }

}
