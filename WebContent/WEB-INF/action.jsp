<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Procédures</title>
	<meta charset="utf-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/bootstrap.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/searchForm.css"/>">
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br><br>
	<span class="erreur">${Form.erreurs['nom_action']}  ${Form.erreurs['année']}</span>
	<form action='<c:url value="/rechercheAction"/>' method="post" class="well form-inline">
		<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>
			<a href='<c:url value="/uploadtoDB"/>' id="link"><i class="fas fa-plus"></i> Ajouter</a>			
		</c:if>
		<input type="text" name="nom_action" value="" class="form-control form-control-sm" placeholder="Action" id="form-elmt0">&emsp;
		<input type="text" name="année" value="" class="form-control form-control-sm" placeholder="Année" id="form-elmt0">&emsp;
		<button type="submit" class="btn btn-primary btn-sm" id="form-elmt0"><i class="fas fa-search"></i> Chercher</button>	
	</form>
	<c:choose>
		<%-- Si aucune action n'existe en session, affichage d'un message par défaut. --%>
		<c:when test="${ empty sessionScope.actions }">
			<p class="alert alert-block alert-danger">Aucune action enregistrée.</p>
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
							<th>LCE</th>
							<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>
								<th></th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ sessionScope.actions }" var="mapActions" varStatus="boucle">
							<tr>
								<td><c:out value="${ mapActions.value.nom_action}"/></td>
								<td>
									<span style="font-size:1.3em; color:Tomato"><i class="fas fa-file-pdf"></i></span>&ensp;
									<a href="<c:url value="/downloadProc"><c:param name="id" value="${mapActions.key }" /></c:url>">
										<c:out value="${mapActions.value.nom_proc}"/>
									</a>
								</td>
								<td>
									<span style="font-size:1.3em; color:Tomato"><i class="fas fa-file-pdf"></i></span>&ensp;
									<a href="<c:url value="/downloadCheck"><c:param name="id" value="${mapActions.key }" /></c:url>">
										<c:out value="${mapActions.value.nom_chlist}"/>
									</a>
								</td>
								<td><c:out value="${ mapActions.value.lce}"/></td>										
								<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>
									<td>
										<a href="<c:url value="/modificationAction"><c:param name="idAction" value="${mapActions.key }" /></c:url>">
											<i class="fas fa-edit"></i>
										</a>&emsp;
										<a href="<c:url value="/suppressionAction"><c:param name="idAction" value="${mapActions.key }" /></c:url>">
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