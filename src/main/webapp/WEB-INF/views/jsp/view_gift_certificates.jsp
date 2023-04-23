<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<html>
<head>
<link href="<c:url value="/resources/css/all.css" />" rel="stylesheet">
</head>
<body>
<h1>Tags List</h1>

<table border="2" cellpadding="2">
<tr><th>Id</th><th>Name</th><th>Description</th><th>Price</th><th>Duration</th><th>Tags</th><th>Date created</th><th>Date last updated</th><th>Action</th></tr>
<c:forEach var="gift_certificate" items="${gift_certificates}">
<tr>
<td>${gift_certificate.id}</td>
<td>${gift_certificate.name}</td>
<td>${gift_certificate.description}</td>
<td>${gift_certificate.price}</td>
<td>${gift_certificate.duration}</td>
<c:set var = "tags" value = "${gift_certificate.tags}" />
<td>${fn:join(tags, ", ")}</td>
<td>${gift_certificate.createDate}</td>
<td>${gift_certificate.lastUpdateDate}</td>
<td><a href="">Delete</a></td>
</tr>
</c:forEach>
</table>
<a href="">Add new gift certificate</a>
</body>
</html>