package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.uri.ExpandSelectTreeCreator;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;

public class ExpandSelectTreeCreatorImpl implements ExpandSelectTreeCreator {

  private final List<SelectItem> initialSelect;
  private final List<ArrayList<NavigationPropertySegment>> initialExpand;

  public ExpandSelectTreeCreatorImpl(final List<SelectItem> select, final List<ArrayList<NavigationPropertySegment>> expand) {
    if (select != null && select.isEmpty() == false) {
      initialSelect = select;
    } else {
      initialSelect = Collections.emptyList();
    }

    if (expand != null && expand.isEmpty() == false) {
      initialExpand = expand;
    } else {
      initialExpand = Collections.emptyList();
    }
  }

  @Override
  public ExpandSelectTreeNode create() {

    //Initial node
    ExpandSelectTreeNodeImpl root = new ExpandSelectTreeNodeImpl();
    if (initialSelect.isEmpty() && !initialExpand.isEmpty()) {
      buildPureExpandTree(root);
    } else {
      try {
        buildCombinedTree(root);
      } catch (EdmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    //    if (!initialSelect.isEmpty() && !initialExpand.isEmpty()) {
    //      try {
    //        buildCombinedTree(root);
    //      } catch (EdmException e) {
    //        // TODO Auto-generated catch block
    //        e.printStackTrace();
    //      }
    //    } else if (!initialSelect.isEmpty() && initialExpand.isEmpty()) {
    //      try {
    //        buildCombinedTree(root);
    //      } catch (EdmException e) {
    //        // TODO Auto-generated catch block
    //        e.printStackTrace();
    //      }
    //      
    //      //buildPureSelectTree(root);
    //    } else if (initialSelect.isEmpty() && !initialExpand.isEmpty()) {
    //      buildPureExpandTree(root);
    //    }

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

  //  private void buildPureSelectTree(final ExpandSelectTreeNodeImpl root) {
  //    for (SelectItem item : initialSelect) {
  //      if (item.getNavigationPropertySegments().isEmpty()) {
  //        if (item.getProperty() != null) {
  //          root.addProperty(item.getProperty());
  //        } else if (item.isStar()) {
  //          root.setAllExplicitly();
  //        } else {
  //          throw new ODataRuntimeException("Empty select item are not allowed!");
  //        }
  //      } else {
  //        EdmNavigationProperty navProperty = item.getNavigationPropertySegments().get(0).getNavigationProperty();
  //        root.addChild(navProperty, null);
  //      }
  //    }
  //  }

  private void buildCombinedTree(final ExpandSelectTreeNodeImpl root) throws EdmException {
    for (SelectItem item : initialSelect) {
      List<NavigationPropertySegment> segmentsList = item.getNavigationPropertySegments();
      ExpandSelectTreeNodeImpl actualNode = root;
      for (int segmentListIndex = 0; segmentListIndex < segmentsList.size(); segmentListIndex++) {
        ExpandSelectTreeNodeImpl childNode = null;
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
              if ((item.getProperty() == null && !item.isStar()) && segmentListIndex == segmentsList.size() - 1) {
                ExpandSelectTreeNodeImpl expandNodes = childNode;
                for (int expandListIndex = segmentListIndex + 1; expandListIndex < singleExpandList.size(); expandListIndex++) {
                  ExpandSelectTreeNode newNode = new ExpandSelectTreeNodeImpl();
                  expandNodes.setAllExplicitly();
                  expandNodes = (ExpandSelectTreeNodeImpl) expandNodes.addChild(singleExpandList.get(expandListIndex).getNavigationProperty(), newNode);
                }
              }
              break;
            }
          }
        }
        actualNode = (ExpandSelectTreeNodeImpl) actualNode.addChild(segmentsList.get(segmentListIndex).getNavigationProperty(), childNode);
        if (actualNode == null) {
          break;
        }
      }

      if (actualNode != null) {
        if (item.getProperty() != null) {
          actualNode.addProperty(item.getProperty());
        } else if (item.isStar()) {
          actualNode.setAllExplicitly();
        }
      }

    }
  }

}

//  private void buildCombinedTree(final ExpandSelectTreeNodeImpl root) throws EdmException {
//    for (SelectItem item : initialSelect) {
//      if (item.getNavigationPropertySegments().isEmpty()) {
//        if (item.getProperty() != null) {
//          root.addProperty(item.getProperty());
//        } else if (item.isStar()) {
//          root.setAllExplicitly();
//        } else {
//          throw new ODataRuntimeException("Empty select item are not allowed!");
//        }
//      } else {
//        List<NavigationPropertySegment> segmentsList = item.getNavigationPropertySegments();
//        ExpandSelectTreeNodeImpl actualNode = root;
//        for (int segmentListIndex = 0; segmentListIndex < segmentsList.size(); segmentListIndex++) {
//          ExpandSelectTreeNodeImpl childNode = null;
//          for (ArrayList<NavigationPropertySegment> singleExpandList : initialExpand) {
//            if (singleExpandList.size() > segmentListIndex) {
//              boolean ok = true;
//              for (int expandListIndex = 0; expandListIndex <= segmentListIndex; expandListIndex++) {
//                if (!segmentsList.get(expandListIndex).getNavigationProperty().getName().equals(singleExpandList.get(expandListIndex).getNavigationProperty().getName())) {
//                  ok = false;
//                  break;
//                }
//              }
//
//              if (ok) {
//                childNode = new ExpandSelectTreeNodeImpl();
//                break;
//              }
//            }
//          }
//          actualNode = (ExpandSelectTreeNodeImpl) actualNode.addChild(segmentsList.get(segmentListIndex).getNavigationProperty(), childNode);
//          if (actualNode == null) {
//            break;
//          }
//
//          if (segmentListIndex == segmentsList.size()) {
//            if (item.getProperty() != null) {
//              actualNode.addProperty(item.getProperty());
//            } else if (item.isStar()) {
//              actualNode.setAllExplicitly();
//            }
//          }
//        }
//
//      }
//    }
//
//  }

