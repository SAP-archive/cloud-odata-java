package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;

/**
 * Creates an optimized combined expression tree out of the expand and select options.
 * @author SAP AG
 */
public class ExpandSelectTreeCreator {

  private final List<SelectItem> initialSelect;
  private final List<ArrayList<NavigationPropertySegment>> initialExpand;

  public ExpandSelectTreeCreator(final List<SelectItem> select, final List<ArrayList<NavigationPropertySegment>> expand) {
    if (select != null) {
      initialSelect = select;
    } else {
      initialSelect = Collections.emptyList();
    }

    if (expand != null) {
      initialExpand = expand;
    } else {
      initialExpand = Collections.emptyList();
    }
  }

  public ExpandSelectTreeNode create() throws EdmException {

    //Initial node
    ExpandSelectTreeNodeImpl root = new ExpandSelectTreeNodeImpl();
    if (initialSelect.isEmpty() && !initialExpand.isEmpty()) {
      buildPureExpandTree(root);
    } else {
      buildCombinedTree(root);
    }
    return root;
  }

  private void buildPureExpandTree(final ExpandSelectTreeNodeImpl root) throws EdmException {
    root.setAllExplicitly();
    ExpandSelectTreeNodeImpl actualNode;
    for (ArrayList<NavigationPropertySegment> navigationPropertySegmentList : initialExpand) {
      actualNode = root;
      for (NavigationPropertySegment segment : navigationPropertySegmentList) {
        ExpandSelectTreeNodeImpl childNode = new ExpandSelectTreeNodeImpl();
        childNode.setAllExplicitly();
        actualNode = (ExpandSelectTreeNodeImpl) actualNode.addChild(segment.getNavigationProperty().getName(), childNode);
      }
    }

  }

  private void buildCombinedTree(final ExpandSelectTreeNodeImpl root) throws EdmException {
    for (SelectItem item : initialSelect) {
      ExpandSelectTreeNodeImpl currentNode = root;
      List<NavigationPropertySegment> segmentsList = item.getNavigationPropertySegments();
      for (int segmentListIndex = 0; segmentListIndex < segmentsList.size(); segmentListIndex++) {
        ExpandSelectTreeNodeImpl childNode = null;
        //Check all expand lists for matches
        for (ArrayList<NavigationPropertySegment> singleExpandList : initialExpand) {
          if (singleExpandList.size() > segmentListIndex) {
            boolean ok = true;
            for (int expandListIndex = 0; expandListIndex <= segmentListIndex; expandListIndex++) {
              if (!segmentsList.get(expandListIndex).getNavigationProperty().getName().equals(singleExpandList.get(expandListIndex).getNavigationProperty().getName())) {
                ok = false;
                break;
              }
            }

            if (ok) {
              childNode = new ExpandSelectTreeNodeImpl();
              //If the whole navigation part of a select item matches expand everything if the expand still has segments left
              if ((item.getProperty() == null && !item.isStar()) && segmentListIndex == segmentsList.size() - 1) {
                ExpandSelectTreeNodeImpl expandNodes = childNode;
                expandNodes.setAllExplicitly();
                for (int expandListIndex = segmentListIndex + 1; expandListIndex < singleExpandList.size(); expandListIndex++) {
                  ExpandSelectTreeNodeImpl newNode = new ExpandSelectTreeNodeImpl();
                  newNode.setAllExplicitly();
                  expandNodes = (ExpandSelectTreeNodeImpl) expandNodes.addChild(singleExpandList.get(expandListIndex).getNavigationProperty().getName(), newNode);
                }
              }
              currentNode.addChild(segmentsList.get(segmentListIndex).getNavigationProperty().getName(), childNode);
            }
          }
        }
        currentNode = (ExpandSelectTreeNodeImpl) currentNode.addChild(segmentsList.get(segmentListIndex).getNavigationProperty().getName(), childNode);
        if (currentNode == null) {
          break;
        }
      }

      if (currentNode != null) {
        if (item.getProperty() != null) {
          currentNode.addProperty(item.getProperty());
        } else if (item.isStar()) {
          currentNode.setAllExplicitly();
        }
      }

    }
  }
}