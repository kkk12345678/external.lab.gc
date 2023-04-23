<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link href="<c:url value="/resources/css/all.css" />" rel="stylesheet">
</head>
<body>
<h1>Tags List</h1>

<table border="2" cellpadding="2">
<tr><th>Id</th><th>Name</th><th>Action</th></tr>
<c:forEach var="tag" items="${tags}">
<tr><td>${tag.id}</td><td>${tag.name}</td><td><a href="">Delete</a></td></tr>
</c:forEach>
</table>
<a href="">Add new tag</a>
</body>
</html>