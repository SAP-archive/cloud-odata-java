<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<title>SAP OData Library</title>
<style type="text/css">
html {
	font-size: 100%;
	-webkit-text-size-adjust: 100%;
	-ms-text-size-adjust: 100%;
}

body {
	margin: 0;
	padding-top: 51px;
	font-family: Verdana, Helvetica, Arial, sans-serif;
	font-size: 13px;
	line-height: 18px;
	color: #333333;
	background-color: #ffffff;
}

a {
	color: #0076cb;
	text-decoration: none;
}

a:focus {
	outline: thin dotted #0076cb;
	outline-offset: -1px;
}

a:hover,a:active {
	outline: 0;
}

a:hover {
	color: #004a7e;
	text-decoration: underline;
}

h1,h2,h3,h4,h5,h6 {
	margin: 9px 0;
	font-family: inherit;
	font-weight: bold;
	line-height: 1;
	color: #f0ab00;
	text-rendering: optimizelegibility;
}

h1 {
	font-size: 36px;
	line-height: 40px;
}

h2 {
	font-size: 30px;
	line-height: 40px;
}

h3 {
	font-size: 24px;
	line-height: 40px;
}

h4 {
	font-size: 18px;
	line-height: 20px;
}

h5 {
	font-size: 14px;
	line-height: 20px;
}

h6 {
	font-size: 12px;
	line-height: 20px;
}

ul {
	padding: 0;
	margin: 0 0 9px 25px;
}

ul ul {
	margin-bottom: 0;
}

li {
	line-height: 18px;
}

hr {
	margin: 18px 0;
	border: 0;
	border-top: 1px solid #cccccc;
	border-bottom: 1px solid #ffffff;
}

strong {
	font-weight: bold;
}

em {
	font-style: italic;
}

pre {
	margin: 10px 0px;
}

pre.error {
	margin: -8px 0px;
	color: #9d261d;
	background-color: #f9f9f9;
}

.article {
	margin: 0 18px;
}

table {
	max-width: 100%;
	background-color: transparent;
	border-collapse: collapse;
	border-spacing: 0;
}

.table {
	width: 100%;
	margin-bottom: 18px;
}

.table th,.table td {
	padding: 8px;
	line-height: 18px;
	text-align: left;
	vertical-align: top;
	border-top: 1px solid #dddddd;
}

.table th {
	font-weight: bold;
}

.table thead th {
	vertical-align: bottom;
}

.table thead th.name {
	width: 180px;
	min-width: 180px;
}

.table thead th.value {
	width: 100%;
}

.table thead:first-child tr:first-child th,.table thead:first-child tr:first-child td
	{
	border-top: 0;
}

.table tbody tr:hover td,.table tbody tr:hover th {
	background-color: #f5f5f5;
}

.table tbody tr.green td {
	background-color: #dff0d8;
}

.table tbody tr.red td {
	background-color: #f2dede;
}

.table tbody tr.blue td {
	background-color: #d9edf7;
}

.code {
	font-family: "Courier New", monospace;
	font-size: 13px;
	line-height: 18px;
}

.code ul {
	list-style: none;
	margin: 0 0 0 1.5em;
	padding: 0;
}

.code li {
	position: relative;
}

.code.json li:after {
	content: ',';
}

.code.json li:last-child:after {
	content: '';
}

.code span {
	white-space: nowrap;
	padding: 2px 1px;
}

.code .property {
	font-weight: bold;
	color: #000000;
}

.code .null {
	color: #9d261d;
}

.code .boolean {
	color: #760a85;
}

.code .numeric {
	color: #0076cb;
}

.code .string {
	color: #247230;
}

.code .deffered {
	color: #666666;
	font-size: 0.9em;
}

.code .toggle {
	position: absolute;
	left: -1em;
	cursor: pointer;
}

.code .tag {
	color: #003283;
}

.code .atn {
	color: #760a85;
}

.code .atv {
	color: #247230;
}

.code .text {
	color: #000000;
}

.code .cdata {
	color: #008080;
}

.code .comment,.code .ns {
	color: #666666;
}

ul.tree,ul.tree ul {
	margin: 0;
	padding: 0;
	list-style-type: none;
	background:
		url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAKAQMAAABPHKYJAAAAA1BMVEWIiIhYZW6zAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH1ggGExMZBky19AAAAAtJREFUCNdjYMAEAAAUAAHlhrBKAAAAAElFTkSuQmCC')
		repeat-y;
}

ul.tree ul {
	margin-left: 10px;
}

ul.tree li {
	clear: both;
	margin: 0;
	padding: 0;
	padding-left: 12px;
	line-height: 20px;
	font-family: "Courier New", monospace;
	font-weight: normal;
	font-size: 14px;
	color: #666666;
	background:
		url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAUAQMAAACK1e4oAAAABlBMVEUAAwCIiIgd2JB2AAAAAXRSTlMAQObYZgAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YIBhQIJYVaFGwAAAARSURBVAjXY2hgQIf/GTDFGgDSkwqATqpCHAAAAABJRU5ErkJggg==')
		no-repeat;
}

ul.tree li.last {
	background: #ffffff
		url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAUAQMAAACK1e4oAAAABlBMVEUAAwCIiIgd2JB2AAAAAXRSTlMAQObYZgAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YIBhQIIhs+gc8AAAAQSURBVAjXY2hgQIf/GbAAAKCTBYBUjWvCAAAAAElFTkSuQmCC')
		no-repeat;
}

#redirection>div {
	text-align: center;
	line-height: 1em;
}

#redirection>div.status {
	font-size: 300px;
	color: #ccc;
}

#redirection>div.reason {
	font-size: 80px;
	color: #1f7fdf;
	font-variant: small-caps;
}

.header {
	overflow: visible;
	color: #666666;
	position: fixed;
	top: 0;
	right: 0;
	left: 0;
	z-index: 1030;
	margin-bottom: 0;
}

.header .nav {
	position: relative;
	width: auto;
	min-height: 38px;
	padding-left: 0;
	padding-right: 0;
	border: 1px solid #d4d4d4;
	border-width: 0 0 1px;
	-webkit-box-shadow: 0 1px 10px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 0 1px 10px rgba(0, 0, 0, 0.1);
	box-shadow: 0 1px 10px rgba(0, 0, 0, 0.1);
	background-color: #fafafa;
	background-image: -webkit-linear-gradient(top, #ffffff, #f2f2f2);
	background-image: -moz-linear-gradient(top, #ffffff, #f2f2f2);
	background-image: -o-linear-gradient(top, #ffffff, #f2f2f2);
	background-image: linear-gradient(to bottom, #ffffff, #f2f2f2);
	background-repeat: repeat-x;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffffff',
		endColorstr='#fff2f2f2', GradientType=0 );
}

.header .nav .logo {
	position: absolute;
	top: 4px;
	right: 18px;
	z-index: 1002;
	background: transparent
		url('data:image/png;base64,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')
		no-repeat;
	width: 60px;
	height: 30px;
}

.header .nav ul {
	margin-left: 0;
	margin-bottom: 18px;
	list-style: none;
}

.header .nav ul>li>a {
	display: block;
}

.header .nav ul>li>a:hover {
	text-decoration: none;
	background-color: #cccccc;
}

.header .nav ul {
	position: relative;
	left: 0;
	display: block;
	float: left;
	margin: 0 10px 0 0;
}

.header .nav ul>li {
	float: left;
}

.header .nav ul>li>a {
	float: none;
	padding: 10px 15px 10px;
	color: #666666;
	text-decoration: none;
	text-shadow: 0 1px 0 #ffffff;
}

.header .nav ul>li>a:focus,.header .nav ul>li>a:hover {
	background-color: transparent;
	color: #333333;
	text-decoration: none;
}

.header .nav ul>li.active>a,.header .nav ul>li.active>a:hover,.header .nav ul>li.active>a:focus
	{
	color: #ffffff;
	text-shadow: 0 1px 0 #999999;
	background-color: #f9c544;
	background-image: -webkit-linear-gradient(top, #ffd671, #f0ab00);
	background-image: -moz-linear-gradient(top, #ffd671, #f0ab00);
	background-image: -o-linear-gradient(top, #ffd671, #f0ab00);
	background-image: linear-gradient(to bottom, #ffd671, #f0ab00);
	background-repeat: repeat-x;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffd671',
		endColorstr='#fff0ab00', GradientType=0 );
	text-decoration: none;
}

.header .nav ul>li>a:focus,.header .nav ul>li.active>a:focus {
	outline: thin dotted #0076cb;
	outline-offset: -1px;
}

.header .nav ul>li.disabled>a {
	color: #999999;
}

.header .nav ul>li.disabled>a:hover {
	textdecoration: none;
	background-color: transparent;
	cursor: default;
}
</style>
</head>
<body>
<div align="right">
	<img align="top" src="pic/SAPLogo.png" />
</div>
<h1>SAP OData Library</h1>
<hr />
<div class="code">
	<%
	  String version = "gen/version.html";
	%>
	<%
	  try {
	%>
	<jsp:include page='<%=version%>' />
	<%
	  } catch (Exception e) {
	%>
	<p>IDE Build</p>
	<%
	  }
	%>
</div>
<hr />
<h2>Reference Scenario</h2>
	<h3>Service Document and Metadata</h3>
	<ul>
		<li><a href="ReferenceScenario.svc?_wadl" target="_blank">wadl</a></li>
		<li><a href="ReferenceScenario.svc/" target="_blank">service document</a></li>
		<li><a href="ReferenceScenario.svc/$metadata" target="_blank">metadata</a></li>
	</ul>
	<h3>EntitySets</h3>
	<ul>
		<li><a href="ReferenceScenario.svc/Employees" target="_blank">Employees</a></li>
		<li><a href="ReferenceScenario.svc/Managers" target="_blank">Managers</a></li>
		<li><a href="ReferenceScenario.svc/Buildings" target="_blank">Buildings</a></li>
		<li><a href="ReferenceScenario.svc/Rooms" target="_blank">Rooms</a></li>
		<li><a href="ReferenceScenario.svc/Container2.Photos" target="_blank">Container2.Photos</a></li>
	</ul>
	<h3>Entities</h3>
	<ul>
		<li><a href="ReferenceScenario.svc/Employees('1')"
			target="_blank">Employees('1')</a></li>
		<li><a href="ReferenceScenario.svc/Managers('1')" target="_blank">Managers('1')</a></li>
		<li><a href="ReferenceScenario.svc/Buildings('1')"
			target="_blank">Buildings('1')</a></li>
		<li><a href="ReferenceScenario.svc/Rooms('1')" target="_blank">Rooms('1')</a></li>
		<li><a href="ReferenceScenario.svc/Container2.Photos(Id=1,Type='image/png')"
			target="_blank">Container2.Photos(Id=1,Type='image/png')</a></li>
	</ul>
		<h3>Csrf Protected Service</h3>
	<ul>
		<li><a href="CsrfReferenceScenario.svc/" target="_blank">service document</a></li>
	</ul>
	
</body>
</html>
