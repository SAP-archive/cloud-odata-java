package com.sap.core.odata.core.ep.consumer;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.entry.ODataEntryImpl;

public class EntryHelper {
  /**
   * Validates that all mandatory properties are found and set in the {@link ODataEntry}.
   * Mandatory are all properties that are no key properties and not nullable.
   * If a mandatory property is missing an {@link EntityProviderException} is thrown.
   * @param eia   entity info which contains the information which properties are mandatory
   * @param entry entry for which the mandatory properties are validated
   * @throws EntityProviderException if a mandatory property is missing
   */
  protected static void validateMandatoryPropertiesAvailable(final EntityInfoAggregator eia, final ODataEntryImpl entry) throws EntityProviderException {
    final List<EntityPropertyInfo> keyPropertyInfos = eia.getKeyPropertyInfos();
    final Map<String, Object> data = entry.getProperties();

    for (final EntityPropertyInfo propertyInfo : eia.getPropertyInfos())
      if (!keyPropertyInfos.contains(propertyInfo)
          && propertyInfo.isMandatory()
          && !data.containsKey(propertyInfo.getName()))
        throw new EntityProviderException(EntityProviderException.MISSING_PROPERTY.addContent(propertyInfo.getName()));
  }
}
