<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<!--<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">-->	
	<link rel="stylesheet" type="text/css" href="<c:url value="/css-img/bootstrap.css"/>">
	<style type="text/css">
		.navbar-custom .navbar-nav .nav-link ,.navbar-brand{
		    color: #F8F8FF;
		}
		
		.navbar-custom .nav-item.active .nav-link,
		.navbar-custom .nav-item:hover .nav-link ,
		.navbar-brand:hover{
		    color: grey;
		}
	</style>
</head>
<body>
	<nav class="navbar navbar-expand-sm bg-dark navbar-custom">
		<a class="navbar-brand" href="#"><img src="<c:url value="/css-img/logo.png"/>" class="img-fluid rounded-circle"> CNESTEN</a>
		<ul class="navbar-nav">
			<li class="nav-item">
			  <a class="nav-link" href="<c:url value="/listeActions"/>">Procédures</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link" href="<c:url value="/listeRapports"/>">Rapports</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link" href="<c:url value="/listeFiches"/>">Fiches</a>
			</li>
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
				  Paramètres
				</a>
				<div class="dropdown-menu">
					<c:if test='${sessionScope.sessionUtilisateur.privilège=="Administrateur" }'>				
						<a class="dropdown-item" href="<c:url value="/listeUtilisateurs"/>">Gestion des comptes</a>
					</c:if>
				</div>
			</li>
			<li class="nav-item">
			  <a class="nav-link" href="<c:url value="/contact"/>">Contact</a>
			</li>
			<li class="nav-item" style="position: relative;left:520px;">
			  <a class="nav-link" href="<c:url value="/deconnexion"/>">
			  	<i class="fas fa-sign-out-alt"></i> Déconnexion
			  </a>
			</li>
		</ul>
	</nav>
	<script src='<c:url value="/scripts/jquery-3.3.1.js"/>'></script>
	<script src='<c:url value="/scripts/bootstrap.js"/>'></script>
</body>
</html>