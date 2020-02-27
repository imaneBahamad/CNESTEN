<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html">
<html>
<head>
	<title>Recherche</title>
	<meta charset="utf-8">
	<!--<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">-->	
	<!--<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css">	-->
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/bootstrap.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">
	<style type="text/css">
		#form-elmt{
			position: relative;left:970px;
		}
	</style>
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br><br>
	<h3 align="center">Résultat de la recherche</h3><br><br>
	<c:choose>
		<%-- Si aucun utilisateur n'existe en session, affichage d'un message par défaut. --%>
		<c:when test="${ empty sessionScope.recherche }">
			<p class="alert alert-block alert-danger">Aucune donnée correspondante à votre recherche.</p>
		</c:when>
		<%-- Sinon, affichage du tableau. --%>
		<c:otherwise>
			<section class="table-responsive">
				<table class="table table-bordered table-striped table-condensed">
					<thead>
						<tr>
							<th>Code</th>
							<th>Nom</th>
							<th>Prénom</th>
							<th>Email</th>
							<th>Privilège</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ sessionScope.recherche }" var="mapRecherche" varStatus="boucle">
							<tr>
								<td><c:out value="${ mapRecherche.value.code_utilisateur}"/></td>
								<td><c:out value="${ mapRecherche.value.nom}"/></td>
								<td><c:out value="${ mapRecherche.value.prenom}"/></td>
								<td><c:out value="${ mapRecherche.value.email}"/></td>
								<td><c:out value="${ mapUtilisateurs.value.privilège}"/></td>																
								<td>
									<a href="<c:url value="/modificationUtilisateur"><c:param name="codeUtilisateur" value="${mapRecherche.key }" /></c:url>">
										<i class="fas fa-edit"></i>
									</a>&emsp;
									<a href="<c:url value="/suppressionUtilisateur"><c:param name="codeUtilisateur" value="${mapRecherche.key }" /></c:url>">
										<i class="fas fa-trash-alt"></i>
									</a>
								</td>				
							</tr>
						</c:forEach>
					</tbody>				
				</table>
			</section>
		</c:otherwise>
	</c:choose>
</body>
</html>