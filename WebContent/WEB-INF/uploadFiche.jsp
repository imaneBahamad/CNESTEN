<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Ajout</title>
	<link rel="stylesheet" href="<c:url value="/css-img/bootstrap.css"/>"/>
	<link rel="stylesheet" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">
	<link rel="stylesheet" href="<c:url value="/css-img/form.css"/>"/>
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br>
	<div class="container">
		<div class="row">
			<div class="col-md-7">
				<form method="post" action='<c:url value="/uploadFiche"/>' enctype="multipart/form-data" class="well">
					<fieldset>
						<legend>Ajout fiche</legend>
						<div class="form-group">
							<label for="nom_action">Nom action <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="text" name="nom_action" id="nom_action" value='<c:out value="${Fiche.action.nom_action }"/>' size="30" maxlength="30" class="form-control"/>
				    		<span class="erreur">${Form.erreurs['nom_action']}</span>				    	
				    	</div>
						<div class="form-group">
							<label for="file_fiche">Fiche (1MB max, .pdf) <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="file" name="file_fiche" id="file_fiche" value='<c:out value="${Fiche.file_fiche }"/>' class="form-control"/>
				    		<span class="erreur">${Form.erreurs['file_fiche']}</span>				    	
				    	</div>
				     	<p class="info">${Form.resultat}</p>
						<button type="reset" class="btn btn-danger float-right">Annuler</button>			
						<button type="submit" class="btn btn-primary float-right"><i class="fas fa-check-circle"></i> Valider</button>			
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</body>
</html>