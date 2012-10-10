package org.odata4j.producer;

import java.util.ArrayList;
import java.util.List;

import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.expression.ExpressionParser;

/**
 * Helps producers determine if a property is $selected and/or $expanded.
 *
 * <p>Note on recursive extensions:
 * The idea here is that when one has an object graph that is a tree of like
 * nodes (such as a class hierarchy), it should be possible to specify a $expand
 * that is applied recursively.
 *
 * <p>Two new custom options are proposed:
 *
 * <p>expandR and selectR
 *
 * <p>ABNF:
 * <pre>
 * expandRQueryOp = "expandR=" recursiveExpandClause *("," recursiveExpandClause)
 * recursiveExpandClause = entityNavProperty "/" expandDepth
 * expandDepth = integer
 *
 * selectRQueryOp = "selectR=" recursiveSelectClause *("," recursiveSelectClause)
 * recursiveSelectClause = rSelectItem *("," recursiveSelectClause)
 * rSelectItem = selectedNavProperty "/" rPropItem
 * rPropItem = "*" / selectedProperty
 * </pre>
 *
 * <p>expandDepth drives the number of traversal iterations.  An expandDepth of 0 is
 * unlimited.  During query processing, the max expandDepth of all recursivExpandClauses
 * is computed and drives processing.
 *
 * <p>example:
 *  expandR=SubTypes/0,Properties/1
 *
 * <p>This says that at each position in the object graph traversal
 * during query we will expand the SubTypes navigation property.  At the first
 * level we will also expand the Properties navigation property
 * <pre>
 * selectR=SubTypes/Namespace,SubTypes/Type
 * </pre>
 * <p>This says that whenever we expand the SubTypes navigation property we will only
 *  include Namespace and Type properties.
 */
public class PropertyPathHelper {

  public static final String OptionExpandR = "expandR";
  public static final String OptionSelectR = "selectR";

  /*
   * Our current path in the navigation.  An empty path means we are currently
   * at the root object.
   */
  private PropertyPath currentNavPath = new PropertyPath("");
  protected List<PropertyPath> selectPaths;
  protected List<PropertyPath> expandPaths;
  protected List<PropertyPath> selectRPaths;
  protected List<RecursivePropertyPath> expandRPaths;

  public PropertyPathHelper() {
    setup((String) null, null, null, null);
  }

  public PropertyPathHelper(QueryInfo qi) {
    setup(qi == null ? null : qi.select, qi == null ? null : qi.expand, null, null);
  }

  public PropertyPathHelper(String select, String expand) {
    setup(select, expand, null, null);
  }

  public PropertyPathHelper(String select, String expand, String selectR, String expandR) {
    setup(select, expand, selectR, expandR);
  }

  public PropertyPathHelper(List<EntitySimpleProperty> select, List<EntitySimpleProperty> expand) {
    setup(select, expand, null, null);
  }

  public PropertyPathHelper(List<EntitySimpleProperty> select, List<EntitySimpleProperty> expand, String selectR, String expandR) {
    setup(select,
        expand,
        selectR != null && selectR.length() > 0 ? ExpressionParser.parseExpand(selectR) : null,
        expandR != null && expandR.length() > 0 ? ExpressionParser.parseExpand(expandR) : null);
  }

  private void setup(String select, String expand, String selectR, String expandR) {
    setup(select != null && select.length() > 0 ? ExpressionParser.parseExpand(select) : null,
        expand != null && expand.length() > 0 ? ExpressionParser.parseExpand(expand) : null,
        selectR != null && selectR.length() > 0 ? ExpressionParser.parseExpand(selectR) : null,
        expandR != null && expandR.length() > 0 ? ExpressionParser.parseExpand(expandR) : null);
  }

  private void setup(List<EntitySimpleProperty> select, List<EntitySimpleProperty> expand,
      List<EntitySimpleProperty> selectR, List<EntitySimpleProperty> expandR) {
    if (select != null && select.size() > 0) {
      selectPaths = new ArrayList<PropertyPath>(select.size());
      for (EntitySimpleProperty p : select) {
        selectPaths.add(new PropertyPath(p.getPropertyName()));
      }
    }

    if (expand != null && expand.size() > 0) {
      expandPaths = new ArrayList<PropertyPath>(expand.size());
      for (EntitySimpleProperty p : expand) {
        expandPaths.add(new PropertyPath(p.getPropertyName()));
      }
    }

    if (selectR != null && selectR.size() > 0) {
      selectRPaths = new ArrayList<PropertyPath>(selectR.size());
      for (EntitySimpleProperty p : selectR) {
        PropertyPath path = new PropertyPath(p.getPropertyName());
        // must have 2 components
        if (path.getNComponents() != 2) {
          throw new RuntimeException("selectR clause must have 2 components: " + p.getPropertyName());
        }
        selectRPaths.add(path);
      }
    }

    if (expandR != null && expandR.size() > 0) {
      expandRPaths = new ArrayList<RecursivePropertyPath>(expandR.size());
      for (EntitySimpleProperty p : expandR) {
        PropertyPath path = new PropertyPath(p.getPropertyName());
        // must have 2 components
        if (path.getNComponents() != 2) {
          throw new RuntimeException("expandR clause must have 2 components: " + p.getPropertyName());
        }
        int depth = 0;
        try {
          depth = Integer.parseInt(path.getLastComponent());
        } catch (Exception ex) {
          throw new RuntimeException("2nd component of expandR clause must be the integer depth: " + p.getPropertyName());
        }
        expandRPaths.add(new RecursivePropertyPath(path.removeLastComponent(), depth));
      }
    }
  }

  /**
   * Returns true if the $select contains any limiting items on the current navPath.
   *
   * @return true if select is limited, false if not
   */
  protected boolean isSelectionLimited() {
    // selection is only limited if the $select explicitly says so.
    if (selectPaths == null) {
      return false;
    }
    // a match starts with navPath and has a single additional component.
    int nComponentsToMatch = currentNavPath.getNComponents() + 1;
    // we search the entire list, a wild match can occur anywhere and trumps
    // any other matches
    for (PropertyPath p : selectPaths) {
      if (p.getNComponents() == nComponentsToMatch) {
        // this is a candidate path that matches in length
        if (p.isWild()) {
          // wild card says don't limit selection.
          return false;
        } else if (p.startsWith(currentNavPath)) {
          // any match means selection is explicitly limited.
          return true;
        }
      }
    }
    return false;
  }

  protected boolean isSelectionLimitedRecursive() {
    // selection is only limited if the selectR explicitly says so.
    if (selectRPaths == null) {
      return false;
    }
    // empty current nav paths:  see design notes.  These are a problem.
    // for now we force them to use $select redundantly...not perfect..
    if (this.currentNavPath.isEmpty()) {
      return false;
    }
    for (PropertyPath p : selectRPaths) {
      // blat/<propname> matches foo/bar/blat matches
      if (p.getFirstComponent().equals(this.currentNavPath.getLastComponent())) {
        // this is a candidate path that matches in length
        if (p.isWild()) {
          // wild card says don't limit selection.
          return false;
        } else {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Determines if the given property is selected on the current navigation path.
   *
   * @param propName  name of a regular property or a navigation property
   * @return true if property is selected, false if not
   */
  public boolean isSelected(String propName) {
    boolean limited = false;
    if (this.isSelectionLimited()) {
      limited = true;
      PropertyPath checkPath = currentNavPath.addComponent(propName);
      for (PropertyPath p : selectPaths) {
        if (p.equals(checkPath)) {
          return true;
        }
      }
    }
    // allow the selectR to override the $select limiters
    if (this.isSelectionLimitedRecursive()) {
      limited = true;
      for (PropertyPath p : this.selectRPaths) {
        // p of:
        //     blat/<propname>
        // matches current of:
        //     .../blat
        if (p.getLastComponent().equals(propName)
            && this.currentNavPath.getNComponents() >= 1
            && this.currentNavPath.getLastComponent().equals(p.getFirstComponent())) {
          return true;
        }
      }
    }
    if (limited) {
      // found one or more limiters but did not find a match
      return false;
    } else {
      // no limiters found, must be selected
      return true;
    }
  }

  /** determines if the given navigation property is expanded on the current navigation path */
  protected boolean isExpandedExplicit(String navPropName) {
    // expand paths don't have wildcarding...hmmh...why not?
    if (expandPaths == null) {
      return false;
    }
    PropertyPath checkPath = currentNavPath.addComponent(navPropName);
    for (PropertyPath p : expandPaths) {
      if (p.equals(checkPath)) {
        return true;
      }
    }
    return false;
  }

  protected boolean isExpandedRecursive(String navPropName) {
    if (expandRPaths == null) {
      return false;
    }
    // recursive expansion doesn't care about the current navigation path.
    for (RecursivePropertyPath p : this.expandRPaths) {
      if (p.getFirstComponent().equals(navPropName) && p.isValidAtDepth(this.getCurrentDepth())) {
        return true;
      }
    }
    return false;
  }

  public boolean isExpanded(String navPropName) {
    return isExpandedExplicit(navPropName) || isExpandedRecursive(navPropName);
  }

  public void navigate(String propName) {
    this.currentNavPath = this.currentNavPath.addComponent(propName);
  }

  public void popPath() {
    this.currentNavPath = this.currentNavPath.removeLastComponent();
  }

  public PropertyPath getCurrentNavPath() {
    return currentNavPath;
  }

  public int getCurrentDepth() {
    // depth numbered from 1
    return currentNavPath.getNComponents() + 1;
  }

  public boolean isRecursive() {
    return selectRPaths != null || expandRPaths != null;
  }

  @Override
  public String toString() {
    return currentNavPath.toString();
  }

}
