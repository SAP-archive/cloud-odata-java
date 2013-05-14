package com.sap.core.odata.core.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext.RuntimeMeasurement;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class ODataDebugResponseWrapper {

  public static final String oDataDebugQueryParameter = "odata-debug";
  public static final String oDataDebugTrue = "true";
  public static final String oDataDebugDownload = "download";

  private ODataResponse response;
  private ODataContextImpl context;
  private final boolean isDownload;

  public ODataDebugResponseWrapper(ODataResponse response, ODataContextImpl context, ODataHttpMethod method, String debugValue) {
    this.response = response;
    this.context = context;
    if (oDataDebugDownload.equals(debugValue)) {
      isDownload = true;
    } else {
      isDownload = false;
    }
  }

  public ODataResponse wrapResponse() {
    try {

      String data = getStringFromInputStream((InputStream) response.getEntity());
      String contentType = response.getHeader("Content-Type");

      String content;
      if (contentType.contains("xml")) {
        content = "<script type=\"text/x-scripty-xml\">" + data + "</script>";
      } else if (data != null && contentType.equals("application/json")) {
        content = "<script type=\"text/x-scripty-json\">" + data + "</script>";
      } else {
        // no further supported formats for now
        return response;
      }

      String title = "<title>" + context.getPathInfo().toString() + "</title>";

      String styles = loadStyles();
      String jQuery = loadScripts("/jquery-1.6.2.js");
      String scripts = loadScripts("/scripts.js");

      String base = "<base href=\"" + context.getPathInfo().getServiceRoot().toASCIIString() + "\">";

      // render box generically
      String box = renderBox(createTabs());

      String html = "<!DOCTYPE HTML>" + "\n" + "<html lang=\"en\">" + "\n" + "<head>" + "\n" + "<meta charset=\"utf-8\">" + "\n" + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge;chrome=1\">" + "\n" + title + "\n" + base + "\n" + styles + "\n" + "</head>" + "\n"
          + "<body>" + "\n" + box + "\n" + content + "\n" + jQuery + "\n" + scripts + "\n" + "<script type=\"text/javascript\">" + "\n" + "$(function(){" + "\n" + "  $('script[type|=\"text/x-scripty\"]').scripty();" + "\n" + "  $('.tab_content').hide();"
          + "\n" + "  $('#trigger').click(function() {" + "\n" + "    var href = $('ul.tabs li.active a').attr('href');" + "\n" + "    window.location.hash = (href) ? href : '#tab1';" + "\n" + "  });" + "\n" + "  $('#box a.close').click(function(e) {"
          + "\n" + "    e.preventDefault();" + "\n" + "    window.location.hash = '#none';" + "\n" + "  });" + "\n" + "  $('ul.tabs li').click(function(e) {" + "\n" + "    e.preventDefault();" + "\n"
          + "    window.location.hash = $(this).find('a').attr('href');" + "\n" + "  });" + "\n" + "  var switchTab = function(hash) {" + "\n" + "    if (/^#tab\\d$/.test(hash)) {" + "\n" + "      $('#box:hidden').fadeIn('fast');" + "\n"
          + "      $(\"ul.tabs li\").removeClass(\"active\");" + "\n" + "      $(\"ul.tabs\").find('a[href=\"' + hash + '\"]').parent('li').addClass(\"active\");" + "\n" + "      $(\".tab_content\").hide().css({opacity: 0.1});" + "\n"
          + "      var maxHeight = $(window).height()*0.85-96;" + "\n" + "      if (maxHeight < $(hash).outerHeight()) {" + "\n" + "        $(hash).show().animate({opacity: 1.0, height: maxHeight},'fast');" + "\n" + "      } else {" + "\n"
          + "        $(hash).show().animate({opacity: 1.0},'fast');" + "\n" + "      }" + "\n" + "    } else {" + "\n" + "      $('#box').fadeOut('fast');" + "\n" + "    }" + "\n" + "  };" + "\n"
          + "  if ((\"onhashchange\" in window) && !($.browser.msie)) {" + "\n" + "    $(window).bind('hashchange', function() {" + "\n" + "      switchTab(window.location.hash);" + "\n" + "    }).trigger('hashchange');" + "\n" + "  } else {" + "\n"
          + "    $(window).data('~hash~', window.location.hash);" + "\n" + "    window.setInterval(function() {" + "\n" + "      if ($(window).data('~hash~') != window.location.hash) {" + "\n" + "        $(window).data('~hash~', window.location.hash);"
          + "\n" + "        switchTab(window.location.hash);" + "\n" + "      }" + "\n" + "    }, 500);" + "\n" + "    switchTab(window.location.hash);" + "\n" + "  }" + "\n" + "  $('tr:odd').addClass('odd');" + "\n"
          + "  $('ul.tree li:last-child').addClass('last');" + "});" + "\n" + "</script>" + "\n" + "</body>" + "\n" + "</html>";
      ;
      return ODataResponse.status(HttpStatusCodes.OK).entity(html).contentHeader(MediaType.TEXT_HTML).build();
      
    } catch (ODataException e) {
      throw new ODataRuntimeException("Should not happen");
    }
  }

  private List<DebugTab> createTabs() {
    List<DebugTab> tabs = new ArrayList<DebugTab>();

    Properties properties = new Properties();

    // request tabstrip
//    Enumeration<?> e = request.getHeaderNames();
//    while (e.hasMoreElements()) {
//      String headerName = (String) e.nextElement();
//      String headerValue = request.getHeader(headerName);
//      properties.setProperty(headerName, headerValue);
//    }

    tabs.add(createTabNvp("Request", "Name", "Value", properties));

    // response tabstrip
    properties.clear();
    setProperty(properties, "Status-Code", Integer.toString(response.getStatus().getStatusCode()));
    //setProperty(properties, "Content-Type", getValuesAsString(response.getHeader("Content-Type")));

    tabs.add(createTabNvp("Response", "Name", "Value", properties));

    // clear response environment
    properties.clear();

    //request tabstrip


    //environment tabstrip
    tabs.add(createTabNvp("Environment", "Name", "Value", properties));

    if (context.getRuntimeMeasurements() != null) {
      tabs.add(createTabRuntime(context.getRuntimeMeasurements()));
    }

    return tabs;
  }

  private void setProperty(Properties properties, String name, String value) {
    if (value == null) {
      value = "";
    }
    properties.setProperty(name, value);
  }

  private String getValuesAsString(List<?> list) {
    String valuesAsString = "";
    for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
      valuesAsString += iterator.next();
      valuesAsString += "\n";
    }

    if (valuesAsString.length() > 0) {
      valuesAsString.substring(0, valuesAsString.length() - 2);
    }

    return valuesAsString;
  }

  private DebugTab createTabNvp(String header, String tHeadName, String tHeadValue, Properties nvp) {
    DebugTabImpl tab = new DebugTabImpl();
    tab.setHeader(header);

    String tabContent = "<table><thead>";
    tabContent = tabContent + "\n<tr><td>" + tHeadName + "</td><td>" + tHeadValue + "</td></tr>\n";

    Enumeration<?> propertyNames = nvp.propertyNames();
    while (propertyNames.hasMoreElements()) {
      String headerName = (String) propertyNames.nextElement();
      String headerValue = nvp.getProperty(headerName);
      tabContent = tabContent + "<tr><td>" + headerName + "</td><td>" + headerValue + "</td></tr>\n";
    }

    tabContent = tabContent + "</tbody></table>";

    tab.setContent(tabContent);

    return tab;
  }

  private DebugTab createTabRuntime(List<RuntimeMeasurement> runtimeMeasurements) {
    RuntimeTree treeNode;
    RuntimeTree previous = null;
    RuntimeTree parent;
    List<RuntimeTree> tree = new ArrayList<RuntimeTree>();
    String node;

    for (Iterator<RuntimeMeasurement> iterator = runtimeMeasurements.iterator(); iterator.hasNext();) {
      RuntimeMeasurement runtimeMeasurement = (RuntimeMeasurement) iterator.next();
      treeNode = new RuntimeTree();
      treeNode.runtimeMeasurement = runtimeMeasurement;
      if (previous != null) {
        parent = previous;
        while (parent != null && parent.runtimeMeasurement.getTimeStopped() > 0 && parent.runtimeMeasurement.getTimeStopped() <= treeNode.runtimeMeasurement.getTimeStarted()) {
          parent = parent.parent;
        }
        treeNode.parent = parent;
      }
      previous = treeNode;
      tree.add(treeNode);
    }

    DebugTabImpl tab = new DebugTabImpl();
    tab.setHeader("Runtime");
    tab.setContent("<ul class=\"tree code\">");

    for (Iterator<RuntimeTree> iterator = tree.iterator(); iterator.hasNext();) {
      RuntimeTree runtimeTree = (RuntimeTree) iterator.next();
      if (runtimeTree.parent == null) {
        node = createRuntimeTreeNode(tree, runtimeTree);
        tab.setContent(tab.getContent() + node);
      }
    }

    tab.setContent(tab.getContent() + "</ul>");

    return tab;
  }

  private String createRuntimeTreeNode(List<RuntimeTree> tree, RuntimeTree treeNode) {
    long time;
    RuntimeTree parent;
    String innerContent = "";

    String content = "\n" + "<li>" + "<span class=\"atn\">" + treeNode.runtimeMeasurement.getClassName() // escaping
        + "</span>" + "-&gt;" + "<span class=\"atv\">" + treeNode.runtimeMeasurement.getMethodName() // escaping
        + "(&hellip;)</span>" + "&nbsp;";

    if (treeNode.runtimeMeasurement.getTimeStopped() == 0) {
      //        content += "<span class=\"null\" title=\"Starttime absolute [ms]\">" + treeNode.runtimeMeasurement.getTime_start() + "</span>";
    } else {
      time = (treeNode.runtimeMeasurement.getTimeStopped() - treeNode.runtimeMeasurement.getTimeStarted()) / 1000;
      content += "<span class=\"numeric\" title=\"Duration brutto [ms]\">" + String.format("%1$,d", time) + "</span>";
    }

    for (Iterator<RuntimeTree> iterator = tree.iterator(); iterator.hasNext();) {
      RuntimeTree runtimeTree = (RuntimeTree) iterator.next();
      if (runtimeTree.parent != null) {
        parent = runtimeTree.parent;
        if (parent.runtimeMeasurement.getTimeStarted() == treeNode.runtimeMeasurement.getTimeStarted()) {
          innerContent += createRuntimeTreeNode(tree, runtimeTree);
        }
      }
    }

    if (innerContent.length() > 0) {
      content += "\n" + "<ul>" + innerContent + "</ul>" + "\n" + "</li>";
    } else {
      content += "</li>";
    }

    return content;
  }

  private String renderBox(List<DebugTab> tabs) {
    String header = "<ul class=\"tabs\">";
    String content = "<div class=\"tab_container\">";

    int counter = 0;
    for (Iterator<DebugTab> iterator = tabs.iterator(); iterator.hasNext();) {
      DebugTab tab = (DebugTab) iterator.next();
      // tab header
      String headerTag = "<li><a href=\"#tab" + counter + "\">" + tab.getName() + "</a></li>";
      header = header + "\n" + headerTag;
      // tab content
      String contentTag = "<div id=\"tab" + counter + "\" class=\"tab_content\">";
      content = content + "\n" + contentTag + "\n" + tab.getContent() + "\n" + "</div>";
      counter++;
    }

    header = header + "</ul>";
    content = content + "</div>";

    return "<div class=\"trigger\"><a id=\"trigger\"></a></div>" + "<div class=\"box\" id=\"box\">" + "<div id=\"box-close\"><a class=\"close\"></a></div>" + "\n" + header + "\n" + content + "\n" + "</div>";
  }

  private String loadScripts(String name) {
    InputStream inputStream = ODataDebugResponseWrapper.class.getResourceAsStream(name);
    return "<script type=\"text/javascript\">" + getStringFromInputStream(inputStream) + "</script>\n";
  }

  static private String getStringFromInputStream(InputStream inputStream) {
    String strText = "";
    if (inputStream != null) {
      StringWriter sw = new StringWriter();
      char[] bufText = new char[1024];
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        int charRead = 0;

        while ((charRead = reader.read(bufText)) != -1) {
          sw.write(bufText, 0, charRead);
        }
        strText = sw.toString();
      } catch (Exception es) {
        es.printStackTrace();
      } finally {
        try {
          reader.close();
        } catch (IOException es) {}
        try {
          inputStream.close();
        } catch (IOException es) {}
        try {
          sw.close();
        } catch (IOException es) {}
      }
    }
    return strText;
  }

  private String loadStyles() {

    String backgroundNode = "    background: url('data:image/png;base64,"
        + "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAUAQMAAACK1e4oAAAABlBMVEUAAwCIiIgd2JB2AAAAAXRSTlMAQObYZgAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YIBhQIJYVaFGwAAAARSURBVAjXY2hgQIf/GTDFGgDSkwqATqpCHAAAAABJRU5ErkJggg==" + "') no-repeat;";

    String backgroundLastNode = "    background: #fff url('data:image/png;base64,"
        + "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAUAQMAAACK1e4oAAAABlBMVEUAAwCIiIgd2JB2AAAAAXRSTlMAQObYZgAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YIBhQIIhs+gc8AAAAQSURBVAjXY2hgQIf/GbAAAKCTBYBUjWvCAAAAAElFTkSuQmCC" + "') no-repeat;";

    String backgroundImageLogo = "    background-image: url('data:image/png;base64," + "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" + "');";

    String backgroundImageClose = "    background-image: url('data:image/png;base64," + "iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAZdEVYdFNvZ"
        + "nR3YXJlAFBhaW50Lk5FVCB2My41Ljg3O4BdAAAHOklEQVRYR62YWWwVZRiG6S411GrdEjVGcCkajXphpSFqqdoFwoXaUrpowDYKlqULpRtpKQkQkrbQhUIkNpHEJg"
        + "TjjXcQEgKmyJLGFMIl3ICFEqBVoBv9fZ/hn3HO6ekmTvJm5sz8yzvf8n7fnDlzZnmsXLkyTIgQooRHhFjhUQuuucczxoTNcvmZD9fi4UKMECc8JTwvvCS8LLxqwTX"
        + "3nrNjGMuc8JnvNM1IaxHe+gnhBSFReFdIFlKET4Q0C64/ss8Y85olzlzWeDiLaYFI4TFL5A27EZt/IRQIRcJaodhijc6F9tnnliTEmYtFsVjkrK3FmwiYGtfgjveF"
        + "TCFXYNOKgoKChsrKyrampqaDBw4cOAwaGxt/rKioaM3Pz29gjB3LnAwhSXhFeFKInjEpSwbzPmvfDBdks3hubm5tQ0PD9ydPnvz9+vXr/cPDwyPj4+PGPbgeGhoav"
        + "nbtWv/x48e76+rq9mtejfCtgFU/tGs+wwvPiJS1DGTeEoiJfKGsvLy8paenp3dkZGTUYzDNBYTPnTv3R0lJyW6tUWrX+ljnNwVITW0pDSBmcBM+h8xXQuW+ffsODQ"
        + "4O/hVqf1nDnD9/3ly4cMHcuHEjJMWBgYHB1tbWLq21WfhSgNTrAu4LHVN6QNwQwMQMbsIylV1dXb+Ojo6O+Xe6ePGi2bhxo1mwYIGJjo42kZGRDmJiYszChQtNdXW"
        + "1uXz5cgA5LNvZ2fmLJZVn3YdMEOgTs083iRvSmgAmZsqwjJ+MrGTWr19voqKijPw/JebOnWsUQ+bevXseMUi1tLT8ZN1HTL0nkH2B8aQbiB5agavIpjXEjAj87a52"
        + "9epVk5SUNC2RYKLp6enm1q1bHqnbt28PbNiwoUl7EOjp1nWPw8ELchha66AZSqbcWgLYXQXLQCY2NtYUFxebFStWmIiIiEnJ4cZVq1aZwsJCx6WZmZlkn0fq9OnTP"
        + "dqnWuBYZK30IMDxn/UjCozorSG1/dm0bt06Z3POHGNjY6a+vj4kKQi0tbUZJABkZWU5c3fs2OERQhpqamo6rJU+1RlFnwcXCFEEySykHr9WoDPubALYjZmcnByHzG"
        + "SkINPe3u4Q4VDamyVLljiE5s2bZ8hI9zh27Nhv2muT8JnwjkDGhUOIykxg4S4JcEGDRM/LX7LJjQvctHXr1gBS/A4PD3dI+8nIwo57eebO37Vrl0dIMdmXl5dXrz3"
        + "JZtxGQY6CENlFhaZQFqkctCNozORN58+fHxArwaTu379v5GLT0dHhWSYUGUgtXrzYI6TsGyotLd2jPb8WUPAXhRgI0cOgBwjh2ubm5oOuyfv6+pygDM4cSEECMhyc"
        + "3TmSCccyYWFhE+bFxcUZnts54zt37uzUnmQbQrkALhCiuUIMScFiFcmf3dfo7e11BC+U5uCKbdu2eaTcuJqMDGvwcgoHZ3lewGoS3QKBTeENSeiwS4iSMBkhYsbvJ"
        + "tdSbkyFegmUvL+/f1pCAS6jnXDNz2QWCV48OJvIPL/7IBVKp+Lj472E0PhxScEPoVzmD+pCehw3qHmVxMTEAELBZIgJ9Mnvvsl0KiUlxQvqu3fvDtkuYEJQT0h7+h"
        + "l3pgh6hPyix3M3mwhgYopAn0qnFDMeoStXrvypilAnC1FoA9LeL4y0nRU0V+7MS5cuGQolblu9evWE1PZnUyidWrp0qTM3ISEhoKYdOXLkhPYqFxDGtwVPGCeUDpW"
        + "F/X63bdmyxVm0qKjIU+Bg0QslnsTV8uXLnbl79+4N0KCqqqp2kfjGZhhZ/qB02HoWUFz1oIZOz12BFiItLc1J2+zsbJOamhqgwKF0atmyZQ4ZslQ9tqc/rNnd3X1W"
        + "e1QJOQLtDir9b/eoH/72I4PIJ+Do9FxSN2/eNBkZGbNuP1QezJ07dzzraJ1bSoJGax23/Yj3rOP2ILpBtlHT+DrIEkppO/0NGi3E9u3bnUI5XYNGzOAmV5lhRBggK"
        + "1q7RCBeadCwzsSGH4YC7SSKSW2h6G2m7QxuYSkpkn2TnJzskMOV6BU6Q2qTTVjUf0BGQnpIa/J5xKfRB8LkLayNJZp8op3OkfpCQ74ZiVen57nPvxGpT1tBSXBTPo"
        + "CJfuAmaxnI8IGZKtDkJwhTfzhqQLTAJwqfKpBCJ0rVhjSfOXOmh+YqeMPJflPRT506ddbGDG7CMpDhhZ9mr9l8m0GKibiPxo2qXF1bW9tBc0U/w4aUALc75BoFRvS"
        + "OHj16Ql8fpHaVQHoTM7gJy0BmZh+KviDHUrgPPxPoZAQHxDbRXJWVle2hhcClgNqkHme3bbwQPYiQ2swlgFkLN83MMsHm00RiikAn+3izRQKtAupK0FOHIPidBdfc"
        + "w82MYSw6w1yy6b/92eAnpkXcPx74VIEYDTk9MORwJ3HGxoBr7vGMcoACM4e5iO/D/R0TRAzxxI1IPK7kjWk76fSQCsA193jGGMYy5/8jEsKNWAxy/r/06KlcYAmeM" + "WbWRP4B7M5tC2EGvCgAAAAASUVORK5CYII=');";

    return "<style type=\"text/css\">" + "\n" + "  body {" + "\n" + "    color: #333;" + "\n" + "    font-family: verdana,arial,sans-serif;" + "\n" + "    font-size: 12px;" + "\n" + "    margin: 0 auto;" + "\n" + "  }" + "\n" + " .right{float: right;} "
        + "\n" + "  #trigger{" + "\n"
        + backgroundImageLogo
        + "\n"
        + "    position:fixed; bottom:3px; right:5px;"
        + "\n"
        + "    height:28px;"
        + "\n"
        + "    width:57px;"
        + "\n"
        + "    z-index:5;"
        + "\n"
        + "    cursor:pointer;"
        + "\n"
        + "  }"
        + "\n"
        + "  /* Style for overlay and box */"
        + "\n"
        + "  .box{"
        + "\n"
        + "    display: none;"
        + "\n"
        + "    position: fixed; top:10%; right:15%; left: 15%;"
        + "\n"
        + "    max-height: 800px;"
        + "\n"
        + "    background-color:#fafbfc;"
        + "\n"
        + "    color:#444;"
        + "\n"
        + "    padding: 20px;"
        + "\n"
        + "    margin: 0;"
        + "\n"
        + "    border:1px solid #999;"
        + "\n"
        + "    -moz-border-radius: 5px;"
        + "\n"
        + "    -webkit-border-radius:5px;"
        + "\n"
        + "    -khtml-border-radius:5px;"
        + "\n"
        + "    -moz-box-shadow: 0 5px 20px #333;"
        + "\n"
        + "    -webkit-box-shadow: 0 5px 20px #333;"
        + "\n"
        + "    z-index:1001;"
        + "\n"
        + "  }"
        + "\n"
        + "  .box-close{"
        + "\n"
        + "    position: relative;"
        + "\n"
        + "    height: 1px;"
        + "\n"
        + "    width: 100%;"
        + "\n"
        + "  }"
        + "\n"
        + "  .box .close{"
        + "\n"
        + backgroundImageClose
        + "\n"
        + "    position: absolute; right: -18px; top: -18px;"
        + "\n"
        + "    cursor: pointer;"
        + "\n"
        + "    height: 36px;"
        + "\n"
        + "    width: 36px;"
        + "\n"
        + "  }"
        + "\n"
        + "  /* Style for tabs */"
        + "\n"
        + "  ul.tabs {"
        + "\n"
        + "    margin: 0;"
        + "\n"
        + "    padding: 0;"
        + "\n"
        + "    float: left;"
        + "\n"
        + "    list-style: none;"
        + "\n"
        + "    height: 32px;"
        + "\n"
        + "    border-bottom: 1px solid #999;"
        + "\n"
        + "    border-left: 1px solid #999;"
        + "\n"
        + "    width: 100%;"
        + "\n"
        + "  }"
        + "\n"
        + "  ul.tabs li {"
        + "\n"
        + "    float: left;"
        + "\n"
        + "    margin: 0;"
        + "\n"
        + "    padding: 0;"
        + "\n"
        + "    height: 31px;"
        + "\n"
        + "    line-height: 31px;"
        + "\n"
        + "    border: 1px solid #999;"
        + "\n"
        + "    border-left: none;"
        + "\n"
        + "    margin-bottom: -1px;"
        + "\n"
        + "    background: #e0e0e0;"
        + "\n"
        + "    overflow: hidden;"
        + "\n"
        + "    position: relative;"
        + "\n"
        + "  }"
        + "\n"
        + "  ul.tabs li a {"
        + "\n"
        + "    text-decoration: none;"
        + "\n"
        + "    color: #000;"
        + "\n"
        + "    display: block;"
        + "\n"
        + "    font-size: 1.2em;"
        + "\n"
        + "    padding: 0 20px;"
        + "\n"
        + "    border: 1px solid #fff;"
        + "\n"
        + "    outline: none;"
        + "\n"
        + "  }"
        + "\n"
        + "  ul.tabs li a:hover {"
        + "\n"
        + "    background: #ccc;"
        + "\n"
        + "  }  "
        + "\n"
        + "  html ul.tabs li.active, html ul.tabs li.active a:hover  {"
        + "\n"
        + "    background: #fff;"
        + "\n"
        + "    border-bottom: 1px solid #fff;"
        + "\n"
        + "  }"
        + "\n"
        + "  .tab_container {"
        + "\n"
        + "    border: 1px solid #999;"
        + "\n"
        + "    border-top: none;"
        + "\n"
        + "    clear: both;"
        + "\n"
        + "    float: left; "
        + "\n"
        + "    width: 100%;"
        + "\n"
        + "    background: #fff;"
        + "\n"
        + "    -moz-border-radius-bottomright: 3px;"
        + "\n"
        + "    -khtml-border-radius-bottomright: 3px;"
        + "\n"
        + "    -webkit-border-bottom-right-radius: 3px;"
        + "\n"
        + "    -moz-border-radius-bottomleft: 3px;"
        + "\n"
        + "    -khtml-border-radius-bottomleft: 3px;"
        + "\n"
        + "    -webkit-border-bottom-left-radius: 3px;"
        + "\n"
        + "  }"
        + "\n"
        + "  .tab_content {"
        + "\n"
        + "    padding: 10px;"
        + "\n"
        + "    font-size: 12px;"
        + "\n"
        + "    height: auto;"
        + "\n"
        + "    overflow-y: auto;"
        + "\n"
        + "  }"
        + "\n"
        + "  .tab_content .item {"
        + "\n"
        + "    margin: 0 0 1em 0;"
        + "\n"
        + "  }"
        + "\n"
        + "  .tab_content .error {"
        + "\n"
        + "    line-height: 1.3em; padding: 5px 0; font-weight: bold;  color: #d12;"
        + "\n"
        + "  }"
        + "\n"
        + "  table {"
        + "\n"
        + "    width: 100%;"
        + "\n"
        + "    border: 1px solid #eaecee;"
        + "\n"
        + "    border-collapse: collapse;"
        + "\n"
        + "    text-align: left;"
        + "\n"
        + "  }"
        + "\n"
        + "  th {"
        + "\n"
        + "    font-weight: bold;"
        + "\n"
        + "    background-color: #eaecee;"
        + "\n"
        + "    white-space: nowrap;"
        + "\n"
        + "  }"
        + "\n"
        + "  td,th {"
        + "\n"
        + "    padding: 8px 5px;"
        + "\n"
        + "    border: 1px solid #eaecee;"
        + "\n"
        + "  }"
        + "\n"
        + "  tr.odd {"
        + "\n"
        + "    background-color:#fafbfc;"
        + "\n"
        + "  }"
        + "\n"
        + "  ul.tree, ul.tree ul {"
        + "\n"
        + "    list-style-type: none;"
        + "\n"
        + "    background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAKAQMAAABPHKYJAAAAA1BMVEWIiIhYZW6zAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH1ggGExMZBky19AAAAAtJREFUCNdjYMAEAAAUAAHlhrBKAAAAAElFTkSuQmCC') repeat-y;"
        + "\n"
        + "    margin: 0; padding: 0;"
        + "\n"
        + "  }"
        + "\n"
        + "  ul.tree ul {"
        + "\n"
        + "    margin-left: 10px;"
        + "\n"
        + "  }"
        + "\n"
        + "  ul.tree li {"
        + "\n"
        + "    margin: 0; padding: 0 12px; line-height: 20px; font-family: monospace; font-size: 14px; color: #333; font-weight: normal;"
        + "\n"
        + backgroundNode
        + "\n"
        + "  }"
        + "\n"
        + "  ul.tree li.last {"
        + "\n"
        + backgroundLastNode
        + "\n"
        + "  }"
        + "\n"
        + "  /* Style for code block */"
        + "\n"
        + "  .code {line-height: 1.3em;}"
        + "\n"
        + "  .code span{white-space: nowrap; padding: 2px 1px; }"
        + "\n"
        + "  .code {font-family: monospace; font-size: 1.1em;}"
        + "\n"
        + "  .code .property {font-weight:bold; color: black;}"
        + "\n"
        + "  .code .null {color: red;}"
        + "\n"
        + "  .code .boolean {color: purple;}"
        + "\n"
        + "  .code .numeric {color: blue;}"
        + "\n"
        + "  .code .string {color: green;}"
        + "\n"
        + "  .code .deffered {color: grey; font-size: 0.9em;}"
        + "\n"
        + "  .code .toggle {position: absolute; left: -1em; cursor: pointer;}"
        + "\n"
        + "  .code .tag {color: darkblue;}"
        + "\n"
        + "  .code .atn {color: purple;}"
        + "\n"
        + "  .code .atv {color: green;}"
        + "\n"
        + "  .code .text {color: black;}"
        + "\n"
        + "  .code .cdata {color: teal;}"
        + "\n"
        + "  .code .comment, .code .ns {color: grey;}"
        + "\n"
        + "  .code li {position: relative;}"
        + "\n"
        + "  .code.json li:after {content: ',';}" + "\n" + "  .code.json li:last-child:after {content: '';}" + "\n" + "  .code ul {list-style: none; margin: 0 0 0 1.5em; padding: 0;}</style>";
  }

  private class RuntimeTree {
    private RuntimeMeasurement runtimeMeasurement;
    private RuntimeTree parent;
  }
}
