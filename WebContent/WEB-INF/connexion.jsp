<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Connexion</title>
	<link rel="stylesheet" href="<c:url value="/css-img/bootstrap.css"/>"/>
	<link rel="stylesheet" href="<c:url value="/css-img/loginForm.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">			
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-4 login-form">
				<img src="<c:url value="/css-img/logo.png" />" style="position:relative;left:90px;bottom:10px;" width="150px" height="150px" class="img-fluid rounded-circle"><br>
				<div class="division">
					<p>Division Exploitation Réacteur (DER)<br>
					Direction des Installations Nucléaires (DIN)<br>
					Centre National de l'Energie des Sciences et de 
			        Techniques Nucléaires (CNESTEN)<p>
		        </div>
				<h2>Authentification</h2>
				<form class="well" method="post" action="<c:url value="/connexion"/>">
					<span class="help-block">${requestScope.messageConnexion}</span>
					<span class="erreur">${Form.erreurs['general']}</span>
					<div class="form-group">
						<label class="sr-only text-uppercase" for="nom">Utilisateur</label>
						<input type="text" id="nom" name="nom" value="<c:out value="${Utilisateur.nom}"/>" size="20" maxlength="30" placeholder="Utilisateur" class="form-control"/>	  
						<span class="erreur" >${Form.erreurs['nom']}</span>					
					</div>
					<div class="form-group">
						<label class="sr-only text-uppercase" for="motdepasse">Mot de passe</label>
						<input type="password" id="motdepasse" name="motdepasse" size="20" maxlength="20" placeholder="Mot de passe" class="form-control"/>
						<span class="erreur" >${Form.erreurs['motdepasse']}</span>					
					</div>
					<p class="info">${Form.resultat}</p>
					<button type="submit" class="btn btn-primary float-right"><i class="fas fa-check-circle"></i> Se connecter</button>			
				</form><br>
			</div>
			<div class="col-md-8 panneau">
				<div id="carrousel" class="carousel slide" data-ride="carousel">
				  <!-- Indicators 
				  <ul class="carousel-indicators">
				    <li data-target="#carrousel" data-slide-to="0" class="active"></li>
				    <li data-target="#carrousel" data-slide-to="1"></li>
				    <li data-target="#carrousel" data-slide-to="2"></li>
   				    <li data-target="#carrousel" data-slide-to="3"></li>
   				    <li data-target="#carrousel" data-slide-to="4"></li>
   				    <li data-target="#carrousel" data-slide-to="5"></li>	    
				  </ul>
				
				  <!-- The slideshow 
				  <div class="carousel-inner">
				    <div class="carousel-item active">
				      <img height="530px" width="730px" src="<c:url value="/css-img/image2.jpg" />">
				    </div>
				    <div class="carousel-item">
				      <img height="530px" width="730px" src="<c:url value="/css-img/image1.jpg"/>">
				    </div>
				    <div class="carousel-item">
				      <img height="530px" width="730px" src="<c:url value="/css-img/image3.jpg"/>">
				    </div>
				    <div class="carousel-item">
				      <img height="530px" width="730px" src="<c:url value="/css-img/image4.jpg"/>">
				    </div>
				    <div class="carousel-item">
				      <img height="530px" width="730px" src="<c:url value="/css-img/image5.jpg"/>">
				    </div>
				    <div class="carousel-item">
				      <img height="530px" width="730px" src="<c:url value="/css-img/image6.jpg"/>">
				    </div>
				  </div>
				
				  <!-- Left and right controls 
				  <a class="carousel-control-prev" href="#carrousel" data-slide="prev">
				    <span class="carousel-control-prev-icon"></span>
				  </a>
				  <a class="carousel-control-next" href="#carrousel" data-slide="next">
				    <span class="carousel-control-next-icon"></span>
				  </a>-->
				</div>
			</div>
		</div>
	</div>
	<script src='<c:url value="/scripts/jquery-3.3.1.js"/>'></script>
	<script src='<c:url value="/scripts/bootstrap.js"/>'></script></body>
</html>