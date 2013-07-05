/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.processor.core.jpa.mock.model;

import com.sap.core.odata.processor.core.jpa.mock.model.JPAEdmMockData.EntityType.EntityTypeA;

public interface JPAEdmMockData {
  /*
   * Edm Complex Type Mock Data
   */
  public interface ComplexType {

    public interface ComplexTypeA {
      public static final String name = "ComplexTypeA";
      public static final Class<ComplexTypeA> clazz = ComplexTypeA.class;

      public interface Property {
        public static final String PROPERTY_A = "A";
        public static final String PROPERTY_B = "B";
        public static final String PROPERTY_C = "C";
      }

    }

    public interface ComplexTypeB {
      public static final String name = "ComplexTypeB";

      public interface Property {
        public static final String PROPERTY_D = "D";
        public static final String PROPERTY_E = "E";
      }

    }
  }

  public interface EntityType
  {
    public interface EntityTypeA
    {
      public static final String name = "SalesOrderHeader";
      public static final Class<EntityTypeA> entityClazz = EntityTypeA.class;

      public interface Property
      {
        public static final String PROPERTY_A = SimpleType.SimpleTypeA.NAME;

      }

    }
  }

  public interface SimpleType
  {
    public interface SimpleTypeA
    {
      public static final String NAME = "SOID";
      public static final Class<String> clazz = String.class;
      public static final Class<EntityTypeA> declaringClazz = EntityType.EntityTypeA.class;
    }
  }
}
