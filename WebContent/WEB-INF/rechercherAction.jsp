<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html">
<html>
<head>
	<title>Recherche</title>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/bootstrap.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br><br>
	<h3 align="center">Résultat de la recherche</h3><br><br>
	<c:choose>
		<%-- Si aucune action n'existe en session, affichage d'un message par défaut. --%>
		<c:when test="${ empty sessionScope.recherche }">
			<p class="alert alert-block alert-danger">Aucune donnée correspondante à votre recherche.</p>
		</c:when>
		<%-- Sinon, affichage du tableau. --%>
		<c:otherwise>
			<section class="table-responsive">
				<table class="table table-bordered table-striped table-condensed">
					<thead>
						<tr>
							<th>Action</th>
							<th>Procédure</th>
							<th>Check list</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ sessionScope.recherche }" var="mapRecherche" varStatus="boucle">
							<tr>
								<td><c:out value="${ mapRecherche.value.nom_action}"/></td>
								<td>
									<span style="font-size:1.3em; color:Tomato"><i class="fas fa-file-pdf"></i></span>&ensp;
									<a href="<c:url value="/downloadProc"><c:param name="id" value="${mapRecherche.key }" /></c:url>">
										<c:out value="${mapRecherche.value.nom_proc}"/>
									</a>
								</td>
								<td>
									<span style="font-size:1.3em; color:Tomato"><i class="fas fa-file-pdf"></i></span>&ensp;
									<a href="<c:url value="/downloadCheck"><c:param name="id" value="${mapRecherche.key }" /></c:url>">
										<c:out value="${mapRecherche.value.nom_chlist}"/>
									</a>
								</td>
								<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>
									<td>
										<a href="<c:url value="/modificationAction"><c:param name="id" value="${mapRecherche.key }" /></c:url>">
											<i class="fas fa-edit"></i>
										</a>&emsp;
										<a href="<c:url value="/suppressionAction"><c:param name="id" value="${mapRecherche.key }" /></c:url>">
											<i class="fas fa-trash-alt"></i>
										</a>
									</td>
								</c:if>			
							</tr>
						</c:forEach>
					</tbody>				
				</table>
			</section>
		</c:otherwise>
	</c:choose>
</body>
</html>