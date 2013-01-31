package com.sap.core.odata.ref.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sap.core.odata.api.commons.HttpContentType;
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

  private static final String ENTITYSET_1_1 = "Employees";
  private static final String ENTITYSET_1_2 = "Teams";
  private static final String ENTITYSET_1_3 = "Rooms";
  private static final String ENTITYSET_1_4 = "Managers";
  private static final String ENTITYSET_1_5 = "Buildings";
  private static final String ENTITYSET_2_1 = "Photos";

  private final DataContainer dataContainer;

  public ScenarioDataSource(final DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }

  @Override
  public List<?> readData(final EdmEntitySet entitySet) throws ODataNotImplementedException, ODataNotFoundException, EdmException {
    if (ENTITYSET_1_1.equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getEmployeeSet().toArray());
    else if (ENTITYSET_1_2.equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getTeamSet().toArray());
    else if (ENTITYSET_1_3.equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getRoomSet().toArray());
    else if (ENTITYSET_1_4.equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getManagerSet().toArray());
    else if (ENTITYSET_1_5.equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getBuildingSet().toArray());
    else if (ENTITYSET_2_1.equals(entitySet.getName()))
      return Arrays.asList(dataContainer.getPhotoSet().toArray());
    else
      throw new ODataNotImplementedException();
  }

  @Override
  public Object readData(final EdmEntitySet entitySet, final Map<String, Object> keys) throws ODataNotImplementedException, ODataNotFoundException, EdmException {
    if (ENTITYSET_1_1.equals(entitySet.getName())) {
      for (final Employee employee : dataContainer.getEmployeeSet())
        if (employee.getId().equals(keys.get("EmployeeId")))
          return employee;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if (ENTITYSET_1_2.equals(entitySet.getName())) {
      for (final Team team : dataContainer.getTeamSet())
        if (team.getId().equals(keys.get("Id")))
          return team;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if (ENTITYSET_1_3.equals(entitySet.getName())) {
      for (final Room room : dataContainer.getRoomSet())
        if (room.getId().equals(keys.get("Id")))
          return room;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if (ENTITYSET_1_4.equals(entitySet.getName())) {
      for (final Manager manager : dataContainer.getManagerSet())
        if (manager.getId().equals(keys.get("EmployeeId")))
          return manager;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if (ENTITYSET_1_5.equals(entitySet.getName())) {
      for (final Building building : dataContainer.getBuildingSet())
        if (building.getId().equals(keys.get("Id")))
          return building;
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    } else if (ENTITYSET_2_1.equals(entitySet.getName())) {
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
    if (ENTITYSET_1_1.equals(targetEntitySet.getName())) {
      ArrayList<Object> data = new ArrayList<Object>();
      if (ENTITYSET_1_2.equals(sourceEntitySet.getName()))
        data.addAll(((Team) sourceData).getEmployees());
      else if (ENTITYSET_1_3.equals(sourceEntitySet.getName()))
        data.addAll(((Room) sourceData).getEmployees());
      else if (ENTITYSET_1_4.equals(sourceEntitySet.getName()))
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

    } else if (ENTITYSET_1_2.equals(targetEntitySet.getName())) {
      if (((Employee) sourceData).getTeam() == null)
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      else
        return ((Employee) sourceData).getTeam();

    } else if (ENTITYSET_1_3.equals(targetEntitySet.getName())) {
      if (ENTITYSET_1_1.equals(sourceEntitySet.getName())) {
        if (((Employee) sourceData).getRoom() == null)
          throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
        else
          return ((Employee) sourceData).getRoom();
      } else if (ENTITYSET_1_5.equals(sourceEntitySet.getName())) {
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

    } else if (ENTITYSET_1_4.equals(targetEntitySet.getName())) {
      if (((Employee) sourceData).getManager() == null)
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      else
        return ((Employee) sourceData).getManager();

    } else if (ENTITYSET_1_5.equals(targetEntitySet.getName())) {
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
  public BinaryData readBinaryData(final EdmEntitySet entitySet, final Object mediaLinkEntryData) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    if (mediaLinkEntryData == null)
      throw new ODataNotFoundException(null);

    if (ENTITYSET_1_1.equals(entitySet.getName()) || ENTITYSET_1_4.equals(entitySet.getName())) {
      final Employee employee = (Employee) mediaLinkEntryData;
      return new BinaryData(employee.getImage(), employee.getImageType());
    } else if (ENTITYSET_2_1.equals(entitySet.getName())) {
      final Photo photo = (Photo) mediaLinkEntryData;
      return new BinaryData(photo.getImage(), photo.getImageType());
    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public void writeBinaryData(final EdmEntitySet entitySet, final Object mediaLinkEntryData, final BinaryData binaryData) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    if (mediaLinkEntryData == null)
      throw new ODataNotFoundException(null);

    if (ENTITYSET_1_1.equals(entitySet.getName()) || ENTITYSET_1_4.equals(entitySet.getName())) {
      final Employee employee = (Employee) mediaLinkEntryData;
      employee.setImage(binaryData.getData());
      employee.setImageType(binaryData.getMimeType());
    } else if (ENTITYSET_2_1.equals(entitySet.getName())) {
      final Photo photo = (Photo) mediaLinkEntryData;
      photo.setImage(binaryData.getData());
      photo.setImageType(binaryData.getMimeType());
    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public Object newDataObject(final EdmEntitySet entitySet) throws ODataNotImplementedException, EdmException {
    if (ENTITYSET_1_1.equals(entitySet.getName())) {
      Employee employee = new Employee();
      employee.setEmployeeName("Employee " + employee.getId());
      employee.setLocation(new Location(null, null, null));
      return employee;
    } else if (ENTITYSET_1_2.equals(entitySet.getName())) {
      Team team = new Team();
      team.setName("Team " + team.getId());
      return team;
    } else if (ENTITYSET_1_3.equals(entitySet.getName())) {
      Room room = new Room();
      room.setName("Room " + room.getId());
      return room;
    } else if (ENTITYSET_1_4.equals(entitySet.getName())) {
      Manager manager = new Manager();
      manager.setEmployeeName("Employee " + manager.getId());
      manager.setLocation(new Location(null, null, null));
      return manager;
    } else if (ENTITYSET_1_5.equals(entitySet.getName())) {
      Building building = new Building();
      building.setName("Building " + building.getId());
      return building;
    } else if (ENTITYSET_2_1.equals(entitySet.getName())) {
      Photo photo = new Photo();
      photo.setType(HttpContentType.APPLICATION_OCTET_STREAM);
      return photo;
    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public void deleteData(final EdmEntitySet entitySet, final Map<String, Object> keys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    final Object data = readData(entitySet, keys);

    if (ENTITYSET_1_1.equals(entitySet.getName()) || ENTITYSET_1_4.equals(entitySet.getName())) {
      if (data instanceof Manager)
        for (Employee employee : ((Manager) data).getEmployees())
          employee.setManager(null);
      final Employee employee = (Employee) data;
      if (employee.getManager() != null)
        employee.getManager().getEmployees().remove(employee);
      if (employee.getTeam() != null)
        employee.getTeam().getEmployees().remove(employee);
      if (employee.getRoom() != null)
        employee.getRoom().getEmployees().remove(employee);
      if (data instanceof Manager)
        dataContainer.getManagerSet().remove(data);
      dataContainer.getEmployeeSet().remove(data);

    } else if (ENTITYSET_1_2.equals(entitySet.getName())) {
      for (Employee employee : ((Team) data).getEmployees())
        employee.setTeam(null);
      dataContainer.getTeamSet().remove(data);

    } else if (ENTITYSET_1_3.equals(entitySet.getName())) {
      for (Employee employee : ((Room) data).getEmployees())
        employee.setRoom(null);
      if (((Room) data).getBuilding() != null)
        ((Room) data).getBuilding().getRooms().remove(data);
      dataContainer.getRoomSet().remove(data);

    } else if (ENTITYSET_1_5.equals(entitySet.getName())) {
      for (Room room : ((Building) data).getRooms())
        room.setBuilding(null);
      dataContainer.getBuildingSet().remove(data);

    } else if (ENTITYSET_2_1.equals(entitySet.getName())) {
      dataContainer.getPhotoSet().remove(data);

    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public void createData(final EdmEntitySet entitySet, final Object data) throws ODataNotImplementedException, EdmException, ODataApplicationException {
    if (ENTITYSET_1_1.equals(entitySet.getName()))
      dataContainer.getEmployeeSet().add((Employee) data);
    else if (ENTITYSET_1_2.equals(entitySet.getName()))
      dataContainer.getTeamSet().add((Team) data);
    else if (ENTITYSET_1_3.equals(entitySet.getName()))
      dataContainer.getRoomSet().add((Room) data);
    else if (ENTITYSET_1_4.equals(entitySet.getName()))
      dataContainer.getManagerSet().add((Manager) data);
    else if (ENTITYSET_1_5.equals(entitySet.getName()))
      dataContainer.getBuildingSet().add((Building) data);
    else if (ENTITYSET_2_1.equals(entitySet.getName()))
      dataContainer.getPhotoSet().add((Photo) data);
    else
      throw new ODataNotImplementedException();
  }

  @Override
  public void deleteRelation(final EdmEntitySet sourceEntitySet, Object sourceData, final EdmEntitySet targetEntitySet, final Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    if (ENTITYSET_1_1.equals(targetEntitySet.getName())) {
      if (ENTITYSET_1_2.equals(sourceEntitySet.getName())) {
        for (Iterator<Employee> iterator = ((Team) sourceData).getEmployees().iterator(); iterator.hasNext();) {
          final Employee employee = iterator.next();
          if (employee.getId().equals(targetKeys.get("EmployeeId"))) {
            employee.setTeam(null);
            iterator.remove();
          }
        }
      } else if (ENTITYSET_1_3.equals(sourceEntitySet.getName())) {
        for (Iterator<Employee> iterator = ((Room) sourceData).getEmployees().iterator(); iterator.hasNext();) {
          final Employee employee = iterator.next();
          if (employee.getId().equals(targetKeys.get("EmployeeId"))) {
            employee.setRoom(null);
            iterator.remove();
          }
        }
      } else if (ENTITYSET_1_4.equals(sourceEntitySet.getName())) {
        for (Iterator<Employee> iterator = ((Manager) sourceData).getEmployees().iterator(); iterator.hasNext();) {
          final Employee employee = iterator.next();
          if (employee.getId().equals(targetKeys.get("EmployeeId"))) {
            employee.setManager(null);
            iterator.remove();
          }
        }
      }

    } else if (ENTITYSET_1_2.equals(targetEntitySet.getName())) {
      ((Employee) sourceData).getTeam().getEmployees().remove(sourceData);
      ((Employee) sourceData).setTeam(null);

    } else if (ENTITYSET_1_3.equals(targetEntitySet.getName())) {
      if (ENTITYSET_1_1.equals(sourceEntitySet.getName())) {
        ((Employee) sourceData).getRoom().getEmployees().remove(sourceData);
        ((Employee) sourceData).setRoom(null);
      } else if (ENTITYSET_1_5.equals(sourceEntitySet.getName())) {
        for (Iterator<Room> iterator = ((Building) sourceData).getRooms().iterator(); iterator.hasNext();) {
          final Room room = iterator.next();
          if (room.getId().equals(targetKeys.get("Id"))) {
            room.setBuilding(null);
            iterator.remove();
          }
        }
      }

    } else if (ENTITYSET_1_4.equals(targetEntitySet.getName())) {
      ((Employee) sourceData).getManager().getEmployees().remove(sourceData);
      ((Employee) sourceData).setManager(null);

    } else if (ENTITYSET_1_5.equals(targetEntitySet.getName())) {
      ((Room) sourceData).getBuilding().getRooms().remove(sourceData);
      ((Room) sourceData).setBuilding(null);

    } else {
      throw new ODataNotImplementedException();
    }
  }

  @Override
  public void writeRelation(final EdmEntitySet sourceEntitySet, Object sourceData, final EdmEntitySet targetEntitySet, final Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
    if (ENTITYSET_1_1.equals(targetEntitySet.getName())) {
      final Employee employee = (Employee) readData(targetEntitySet, targetKeys);
      if (ENTITYSET_1_2.equals(sourceEntitySet.getName())) {
        if (employee.getTeam() != null)
          employee.getTeam().getEmployees().remove(employee);
        employee.setTeam((Team) sourceData);
        ((Team) sourceData).getEmployees().add(employee);
      } else if (ENTITYSET_1_3.equals(sourceEntitySet.getName())) {
        if (employee.getRoom() != null)
          employee.getRoom().getEmployees().remove(employee);
        employee.setRoom((Room) sourceData);
        ((Room) sourceData).getEmployees().add(employee);
      } else if (ENTITYSET_1_4.equals(sourceEntitySet.getName())) {
        if (employee.getManager() != null)
          employee.getManager().getEmployees().remove(employee);
        employee.setManager((Manager) sourceData);
        ((Manager) sourceData).getEmployees().add(employee);
      }

    } else if (ENTITYSET_1_2.equals(targetEntitySet.getName())) {
      final Team team = (Team) readData(targetEntitySet, targetKeys);
      if (((Employee) sourceData).getTeam() != null)
        ((Employee) sourceData).getTeam().getEmployees().remove(sourceData);
      ((Employee) sourceData).setTeam(team);
      team.getEmployees().add((Employee) sourceData);

    } else if (ENTITYSET_1_3.equals(targetEntitySet.getName())) {
      final Room room = (Room) readData(targetEntitySet, targetKeys);
      if (ENTITYSET_1_1.equals(sourceEntitySet.getName())) {
        if (((Employee) sourceData).getRoom() != null)
          ((Employee) sourceData).getRoom().getEmployees().remove(sourceData);
        ((Employee) sourceData).setRoom(room);
        room.getEmployees().add((Employee) sourceData);
      } else if (ENTITYSET_1_5.equals(sourceEntitySet.getName())) {
        if (room.getBuilding() != null)
          room.getBuilding().getRooms().remove(room);
        room.setBuilding((Building) sourceData);
        ((Building) sourceData).getRooms().add(room);
      }

    } else if (ENTITYSET_1_4.equals(targetEntitySet.getName())) {
      final Manager manager = (Manager) readData(targetEntitySet, targetKeys);
      if (((Employee) sourceData).getManager() != null)
        ((Employee) sourceData).getManager().getEmployees().remove(sourceData);
      ((Employee) sourceData).setManager(manager);
      manager.getEmployees().add((Employee) sourceData);

    } else if (ENTITYSET_1_5.equals(targetEntitySet.getName())) {
      final Building building = (Building) readData(targetEntitySet, targetKeys);
      if (((Room) sourceData).getBuilding() != null)
        ((Room) sourceData).getBuilding().getRooms().remove(sourceData);
      ((Room) sourceData).setBuilding(building);
      building.getRooms().add((Room) sourceData);

    } else {
      throw new ODataNotImplementedException();
    }
  }
}
