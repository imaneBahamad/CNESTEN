<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset=UTF-8>
	<title>Contact</title>
	<link rel="stylesheet" type="text/css" href='<c:url value="/css-img/bootstrap.css"/>'>
	<link rel="stylesheet" type="text/css" href='<c:url value="/fontawesome-free-5.0.13/web-fonts-with-css/css/fontawesome-all.css"/>'>
	<style type="text/css">
		.container fieldset {
			border: 2px solid #208fff;
			border-radius: 12px;
		}
		
		.container fieldset legend{
			width: auto;
			min-width: auto;
			font-size: x-large;
			font-weight: bold;
			color: #208fff;
		}
		
		.container fieldset pre{
			font-size: medium;
		}
	</style>
</head>
<body>
	<c:import url="/inc/header.jsp"/><br><br><br>
	<div class="container">
		<fieldset>
			<legend><i class="fas fa-phone"></i>&emsp;Dr. Bilal El Bakkari</legend><br>
			<pre>
	        Adjoint au Chef d’Installation
	        Chef de la Division Exploitation Réacteur (DER)
	        Direction des Installations Nucléaires (DIN)
	        Centre National de l'Energie des Sciences et de 
	        Techniques Nucléaires (CNESTEN)
	        B.P. 1382 R.P. 10001 Rabat Maroc
	        <b>Phone :</b> +212 661401874
	        <b>Fax   :</b> +212 537803326
	        <b>Email :</b> bakkari@cnesten.org.ma
			</pre>
		</fieldset>
	</div>

</body>
</html>