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
				<form method="post" action='<c:url value="/ajoutUtilisateur"/>' class="well">
					<fieldset>
						<legend>Ajout utilisateur</legend>
						<div class="form-group">
							<label for="nom">Nom <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="text" name="nom" id="nom" value='<c:out value="${Utilisateur.nom }"/>' size="30" maxlength="30" class="form-control"/>
				    		<span class="erreur">${Form.erreurs['nom']}</span>
				    	</div>
				    	<div class="form-group">
							<label for="prenom">Prénom</label>&emsp;&emsp;
				    		<input type="text" name="prenom" id="prenom" value='<c:out value="${Utilisateur.prenom }"/>' size="30" maxlength="30" class="form-control"/>
				    		<span class="erreur">${Form.erreurs['prenom']}</span>
				    	</div>		
				    	<div class="form-group">
							<label for="email">Email <span class="erreur">*</span></label>&emsp;&emsp;
				    		<input type="email" name="email" id="email" value='<c:out value="${Utilisateur.email }"/>' size="30" maxlength="30" class="form-control"/>
				    		<span class="erreur">${Form.erreurs['email']}</span>				    	
				    	</div>
						<div class="form-group">
							<label for="motdepasse">Mot de passe <span class="erreur">*</span></label>
						 	<input type="password" id="motdepasse" name="motdepasse" size="20" maxlength="20" class="form-control"/>
							<span class="erreur">${Form.erreurs['motdepasse']}</span>						
						</div>
						<div class="form-group">  			
							<label for="confirmation">Confirmation mot de passe <span class="erreur">*</span></label>
							<input type="password" id="confirmation" name="confirmation" size="20" maxlength="20" class="form-control"/>
							<span class="erreur">${Form.erreurs['confirmation']}</span>								
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