<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Rapports</title>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/bootstrap.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/searchForm.css"/>">
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br><br>
	<span class="erreur">${Form.erreurs['nom_action']}</span>	
	<form action='<c:url value="/rechercheRap"/>' method="post" class="well form-inline">
		<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>				
			<a href='<c:url value="/uploadRap"/>' id="link"><i class="fas fa-plus"></i> Ajouter</a>			
		</c:if>
		<input type="text" name="nom_action" value="" class="form-control form-control-sm" placeholder="Action" id="form-elmt">&emsp;
		<button type="submit" class="btn btn-primary btn-sm" id="form-elmt"><i class="fas fa-search"></i> Chercher</button>	
	</form>
	<c:choose>
		<%-- Si aucun rapport n'existe en session, affichage d'un message par défaut. --%>
		<c:when test="${ empty sessionScope.rapports }">
			<p class="alert alert-block alert-danger">Aucun rapport enregistré.</p>
		</c:when>
		<%-- Sinon, affichage du tableau. --%>
		<c:otherwise>
			<section class="table-responsive">
				<table class="table table-bordered table-striped table-condensed">
					<thead>
						<tr>
							<th>Action</th>
							<th>Rapport</th>
							<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>									
								<th></th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ sessionScope.rapports }" var="mapRapports" varStatus="boucle">
							<tr>
								<td><c:out value="${ mapRapports.value.action.nom_action}"/></td>
								<td>
									<span style="font-size:1.3em; color:Tomato"><i class="fas fa-file-pdf"></i></span>&ensp;
									<a href="<c:url value="/downloadRap"><c:param name="idRap" value="${mapRapports.key }" /></c:url>">
										<c:out value="${mapRapports.value.nom_rap}"/>
									</a>
								</td>
								<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>										
									<td>
										<a href="<c:url value="/modificationRap"><c:param name="idRap" value="${mapRapports.key }" /></c:url>">
											<i class="fas fa-edit"></i>
										</a>&emsp;
										<a href="<c:url value="/suppressionRap"><c:param name="idRap" value="${mapRapports.key }" /></c:url>">
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