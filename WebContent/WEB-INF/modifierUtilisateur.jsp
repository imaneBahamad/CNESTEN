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
				<form method="post" action='<c:url value="/modificationUtilisateur"/>' class="well">
					<fieldset>
						<legend>Modification utilisateur</legend>
						<div class="form-group">
							<label for="code_utilisateur">Code</label>&emsp;&emsp;
				    		<input type="text" name="code_utilisateur" id="code_utilisateur" value='<c:out value="${utilisateur.code_utilisateur }"/>' size="30" maxlength="20" readonly="readonly" class="form-control"/>
				    	</div>
						<div class="form-group">
							<label for="nom">Nom <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="text" name="nom" id="nom" value='<c:out value="${utilisateur.nom }"/>' size="30" maxlength="30" class="form-control"/>
				    		<span class="erreur">${Form.erreurs['nom']}</span>
				    	</div>
				    	<div class="form-group">
							<label for="prenom">Prénom</label>&emsp;&emsp;
				    		<input type="text" name="prenom" id="prenom" value='<c:out value="${utilisateur.prenom }"/>' size="30" maxlength="30" class="form-control"/>
				    		<span class="erreur">${Form.erreurs['prenom']}</span>
				    	</div>		
				    	<div class="form-group">
							<label for="email">Email <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="email" name="email" id="email" value='<c:out value="${utilisateur.email }"/>' size="30" maxlength="30" class="form-control"/>
				    		<span class="erreur">${Form.erreurs['email']}</span>				    	
				    	</div>
						<div class="form-group">
							<label for="privilège">Privilège <span class="erreur">*</span></label>
							<select name="privilège" class="form-control">
								<option value="Administrateur">Administrateur</option>
								<option value="Utilisateur simple" selected="selected">Utilisateur simple</option>
							</select>
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