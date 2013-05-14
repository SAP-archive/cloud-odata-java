/*!
 * jQuery Scripty Plugin
 * http://www.sap.com/
 *
 * version : 0.1.1
 * author  : Holger Koser
 * date    : Tue Nov 22 14:53:27 2011 CEST
 */
;(function($) {
  $.fn.scripty = function(options) {
	return this.each(function() {
	  try {
		var $code = $(document.createElement('div')).addClass('code');
		$(this).before($code).remove();
		var content = $(this).html();
		var match = /(?:application|text)\/x-scripty-(.*)/.exec($(this).attr('type'));
		var format = (match) ? match[1] : null;
		var html = null;
		if (format == 'xml') {
		  var xmlDoc = $.parseXML(content);
		  html = (xmlDoc) ? formatXML(xmlDoc) : null;
		} else if (format = 'json') {
		  var jsonObj = $.parseJSON(content);
		  html = (jsonObj) ? formatJSON(jsonObj) : null;
		}
		if (html) {
		  $code.addClass(format).html(html).find('.list').each(function() {
			if (this.parentNode.nodeName == 'LI') {
			  $(document.createElement('div')).addClass("toggle").text("-").click(function() {
				var target = $(this).siblings('.list:first');
				if (target.size() != 1)
				  return;
				if (target.is(':hidden')) {
				  target.show().siblings('.deffered').remove();
				} else {
				  target.hide().before($(document.createElement('span')).attr("class", "deffered").html("..."));
				}
				$(this).text($(this).text() == '-' ? '+' : '-');
			  }).insertBefore($(this.parentNode).children(':first'));
			}
		  });
		}
	  } catch (e) {
		console.log(e);
	  }
	  /* encode html */
	  function encodeHtml(html) {
		return (html != null) ? html.toString().replace(/&/g, "&amp;").replace(/"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;") : '';
	  }
	  /* convert json to html */
	  function formatJSON(value) {
		var typeofValue = typeof value;
		if (value == null) {
		  return '<span class="null">null</span>';
		} else if (typeofValue == 'number') {
		  return '<span class="numeric">' + encodeHtml(value) + '</span>';
		} else if (typeofValue == 'string') {
		  if (/^(http|https):\/\/[^\s]+$/.test(value)) {
			return '<a href="' + value + '">' + encodeHtml(value) + '</a>';
		  } else {
			return '<span class="string">"' + encodeHtml(value) + '"</span>'
		  }
		} else if (typeofValue == 'boolean') {
		  return '<span class="boolean">' + encodeHtml(value) + '</span>'
		} else if (value && value.constructor == Array) {
		  return formatArray(value);
		} else if (typeofValue == 'object') {
		  return formatObject(value);
		} else {
		  return '';
		}
		function formatArray(json) {
		  var html = '';
		  for ( var prop in json)
			html += '<li>' + formatJSON(json[prop]) + '</li>';
		  return (html) ? '<span class="array">[</span><ul class="array list">' + html + '</ul><span class="array">]</span>' : '<span class="array">[]</span>'

		}
		function formatObject(json) {
		  var html = '';
		  for ( var prop in json)
			html += '<li><span class="property">' + encodeHtml(prop) + '</span>: ' + formatJSON(json[prop]) + '</li>';
		  return (html) ? '<span class="obj">{</span><ul class="obj list">' + html + '</ul><span class="obj">}</span>' : '<span class="obj">{}</span>';
		}
	  }
	  /* convert xml to html */
	  function formatXML(document) {
		return formatElement(document.documentElement);
		function formatElement(element) {
		  var html = '<span>&lt;</span><span class="tag">' + encodeHtml(element.nodeName) + '</span>';
		  if (element.attributes && element.attributes.length > 0) {
			html += formatAttributes(element);
		  }
		  if (element.childNodes && element.childNodes.length > 0) {
			html += '<span>&gt;</span>';
			if (element.childNodes.length == 1 && element.childNodes[0].nodeType == 3) {
			  html += '<span class="text">' + encodeHtml(element.childNodes[0].nodeValue) + '</span>';
			} else {
			  html += formatChildNodes(element.childNodes);                    
			} 
			html += '<span>&lt;/</span><span class="tag">' + encodeHtml(element.nodeName) + '</span><span>&gt;</span>';
		  } else {
			html += '<span>/&gt;</span>';
		  } 
		  return html;                
		}
		function formatChildNodes(childNodes) {
		  html = '<ul class="list">';
		  for ( var i = 0; i < childNodes.length; i++) {
			var node = childNodes[i];
			if (node.nodeType == 1) {
			  html += '<li>' + formatElement(node) + '</li>';
			} else if (node.nodeType == 3 && !/^\s+$/.test(node.nodeValue)) {
			  html += '<li><span class="text">' + encodeHtml(node.nodeValue) + '</span></li>';
			} else if (node.nodeType == 4) {
			  html += '<li><span class="cdata">&lt;![CDATA[' + encodeHtml(node.nodeValue) + ']]&gt;</span></li>';
			} else if (node.nodeType == 8) {
			  html += '<li><span class="comment">&lt;!--' + encodeHtml(node.nodeValue) + '--&gt;</span></li>';
			}
		  }
		  html += '</ul>';
		  return html;
		}
		function formatAttributes(element) {
		  var html = '';
		  for (var i = 0; i < element.attributes.length; i++) {
			var attribute = element.attributes[i];
			if (/^xmlns:[^\s]+$/.test(attribute.nodeName)) {
			  html += ' <span class="ns">' + encodeHtml(attribute.nodeName) + '="' + encodeHtml(attribute.nodeValue) + '"</span>';
			} else {
			  html += ' <span class="atn">' + encodeHtml(attribute.nodeName) + '</span>=';
			  if (attribute.nodeName == 'href' || attribute.nodeName == 'src') {
				var separator = (attribute.nodeValue.indexOf('?') == -1) ? '?' : '&';
				var href = (element.baseURI && attribute.nodeValue[0] != '/') ? element.baseURI + attribute.nodeValue : attribute.nodeValue;
				html += '"<a class="link" href="' + href + separator + 'sap-ds-debug=true">' + encodeHtml(attribute.nodeValue) + '</a>"';                    
			  } else {
				html += '"<span class="atv">' + encodeHtml(attribute.nodeValue) + '</span>"';
			  }                
			}   
		  }
		  return html;
		}
	  }
	});
  };
})(jQuery);