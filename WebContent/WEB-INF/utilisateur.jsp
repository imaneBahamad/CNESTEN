<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Utilisateurs</title>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/bootstrap.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/searchForm.css"/>">
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br><br>
	<span class="erreur">${Form.erreurs['nom']}</span>
	<form action='<c:url value="/rechercheUtilisateur"/>' method="post" class="well form-inline">
		<a href='<c:url value="/ajoutUtilisateur"/>' id="link"><i class="fas fa-plus"></i> Ajouter</a>	
		<input type="text" name="nom" value="" class="form-control form-control-sm" placeholder="Utilisateur" id="form-elmt">&emsp;
		<button type="submit" class="btn btn-primary btn-sm" id="form-elmt"><i class="fas fa-search"></i> Chercher</button>	
	</form>
	<c:choose>
		<%-- Si aucun utilisateur n'existe en session, affichage d'un message par défaut. --%>
		<c:when test="${ empty sessionScope.utilisateurs }">
			<p class="alert alert-block alert-danger">Aucun utilisateur enregistré.</p>
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
						<c:forEach items="${ sessionScope.utilisateurs }" var="mapUtilisateurs" varStatus="boucle">
							<tr>
								<td><c:out value="${ mapUtilisateurs.value.code_utilisateur}"/></td>
								<td><c:out value="${ mapUtilisateurs.value.nom}"/></td>
								<td><c:out value="${ mapUtilisateurs.value.prenom}"/></td>
								<td><c:out value="${ mapUtilisateurs.value.email}"/></td>
								<td><c:out value="${ mapUtilisateurs.value.privilège}"/></td>								
								<td>
									<a href="<c:url value="/modificationUtilisateur"><c:param name="codeUtilisateur" value="${mapUtilisateurs.key }" /></c:url>">
										<i class="fas fa-edit"></i>
									</a>&emsp;
									<a href="<c:url value="/suppressionUtilisateur"><c:param name="codeUtilisateur" value="${mapUtilisateurs.key }" /></c:url>">
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