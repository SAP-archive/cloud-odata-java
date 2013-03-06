package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.ExpandSelectTreeCreator;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class ExpandSelectTreeCreatorImpl implements ExpandSelectTreeCreator {

  private final List<SelectItem> initialSelect;
  private final List<ArrayList<NavigationPropertySegment>> initialExpand;

  public ExpandSelectTreeCreatorImpl(final List<SelectItem> select, final List<ArrayList<NavigationPropertySegment>> expand) {
    if (select != null && select.isEmpty() == false) {
      initialSelect = select;
    } else {
      initialSelect = null;
    }

    if (expand != null && expand.isEmpty() == false) {
      initialExpand = expand;
    } else {
      initialExpand = null;
    }
  }

  @Override
  public ExpandSelectTreeNode create() {

    //Initial node
    ExpandSelectTreeNodeImpl root = new ExpandSelectTreeNodeImpl();
    if (initialSelect != null && initialExpand != null) {
      buildCombinedTree(root);
    } else if (initialSelect != null && initialExpand == null) {
      buildPureSelectTree(root);
    } else if (initialSelect == null && initialExpand != null) {
      buildPureExpandTree(root);
    }

    return root;
  }

  private void buildPureExpandTree(final ExpandSelectTreeNodeImpl root) {
    root.setAllExplicitly();
    ExpandSelectTreeNodeImpl actualNode;
    for (ArrayList<NavigationPropertySegment> navigationPropertySegmentList : initialExpand) {
      actualNode = root;
      for (NavigationPropertySegment segment : navigationPropertySegmentList) {
        ExpandSelectTreeNodeImpl childNode = new ExpandSelectTreeNodeImpl();
        childNode.setAllExplicitly();
        actualNode = (ExpandSelectTreeNodeImpl) actualNode.addChild(segment.getNavigationProperty(), childNode);
      }
    }

  }

  private void buildPureSelectTree(final ExpandSelectTreeNodeImpl root) {
    for (SelectItem item : initialSelect) {
      if (item.getNavigationPropertySegments().isEmpty()) {
        if (item.getProperty() != null) {
          root.addProperty(item.getProperty());
        } else if (item.isStar()) {
          root.setAllExplicitly();
        } else {
          throw new ODataRuntimeException("Empty select item are not allowed!");
        }
      } else {
        EdmNavigationProperty navProperty = item.getNavigationPropertySegments().get(0).getNavigationProperty();
        root.addChild(navProperty, null);
      }
    }
  }

  private void buildCombinedTree(final ExpandSelectTreeNodeImpl root) {
    // TODO Auto-generated method stub

  }

}
