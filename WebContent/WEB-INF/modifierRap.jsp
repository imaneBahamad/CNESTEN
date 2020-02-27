<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Modification</title>
	<link rel="stylesheet" href="<c:url value="/css-img/bootstrap.css"/>"/>
	<link rel="stylesheet" href="<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>">
	<link rel="stylesheet" href="<c:url value="/css-img/form.css"/>"/>
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br>
	<div class="container">
		<div class="row">
			<div class="col-md-7">
				<form method="post" action='<c:url value="/modificationRap"/>' enctype="multipart/form-data" class="well">
					<fieldset>
						<legend>Modification rapport</legend>
						<div class="form-group">
							<label for="id_rap">Identifiant</label>&emsp;&emsp;
				    		<input type="text" name="id_rap" id="id_rap" value='<c:out value="${rapport.id_rap }"/>' size="30" maxlength="20" readonly="readonly" class="form-control"/>
				    	</div>
						<div class="form-group">
							<label for="nom_action">Nom action <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="text" name="nom_action" id="nom_action" value='<c:out value="${rapport.action.nom_action }"/>' size="30" maxlength="30" class="form-control"/>
							<span class="erreur">${Form.erreurs['nom_action']}</span>				    	
				    	</div>
						<div class="form-group">
							<label for="file_rap">Rapport (1MB max, .pdf) <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="file" name="file_rap" id="file_rap" value='<c:out value="${rapport.file_rap }"/>' class="form-control"/>
				    		<span class="erreur">${Form.erreurs['file_rap']}</span>				    	
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