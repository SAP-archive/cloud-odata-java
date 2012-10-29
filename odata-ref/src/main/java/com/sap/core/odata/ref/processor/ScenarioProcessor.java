package com.sap.core.odata.ref.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;
import com.sap.core.odata.ref.model.Building;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.model.Employee;
import com.sap.core.odata.ref.model.Manager;
import com.sap.core.odata.ref.model.Room;
import com.sap.core.odata.ref.model.Team;

public class ScenarioProcessor extends ODataSingleProcessor {

  private static final Logger log = LoggerFactory.getLogger(ScenarioProcessor.class);
  private DataContainer dataContainer = new DataContainer();

  @Override
  public ODataResponse readEntitySet(GetEntitySetView uriParserResultView) throws ODataError {

    dataContainer.reset();
    String entitySetName = uriParserResultView.getTargetEntitySet().getName();

    String entity;
    if ("Employees".equals(entitySetName)) {
      entity = "Employees";
    } else if ("Managers".equals(entitySetName)) {
      entity = "Managers";
    } else if ("Buildings".equals(entitySetName)) {
      entity = "Buildings";
    } else if ("Rooms".equals(entitySetName)) {
      entity = "Rooms";
    } else if ("Teams".equals(entitySetName)) {
      entity = "Teams";
    } else {
      entity = "";
    }

    ODataResponseBuilder responseBuilder = ODataResponseBuilder.newInstance();
    return responseBuilder.status(HttpStatus.OK).entity(entity).build();
  }

  @Override
  public ODataResponse readEntity(GetEntityView uriParserResultView) throws ODataError {
    
    dataContainer.reset();
    String entitySetName = uriParserResultView.getTargetEntitySet().getName();
    KeyPredicate key = uriParserResultView.getKeyPredicates().get(0);

    String entity = "Not found";
    if ("Employees".equals(entitySetName)) {
      for (Employee employee : dataContainer.getEmployeeSet()) {
        if (employee.getId().equals(key.getLiteral())) {
          entity = "found";
          break;
        }
      }
    } else if ("Managers".equals(entitySetName)) {
      for (Manager manager : dataContainer.getManagerSet()) {
        if (manager.getId().equals(key.getLiteral())) {
          entity = "found";
          break;
        }
      }
    } else if ("Buildings".equals(entitySetName)) {
      for (Building building : dataContainer.getBuildingSet()) {
        if (building.getId().equals(key.getLiteral())) {
          entity = "found";
          break;
        }
      }
    } else if ("Rooms".equals(entitySetName)) {
      for (Room room : dataContainer.getRoomSet()) {
        if (room.getId().equals(key.getLiteral())) {
          entity = "found";
          break;
        }
      }
    } else if ("Teams".equals(entitySetName)) {
      for (Team team : dataContainer.getTeamSet()) {
        if (team.getId().equals(key.getLiteral())) {
          entity = "found";
          break;
        }
      }
    }

    ODataResponseBuilder responseBuilder = ODataResponseBuilder.newInstance();
    return responseBuilder.status(HttpStatus.OK).entity(entity).build();
  }

}
