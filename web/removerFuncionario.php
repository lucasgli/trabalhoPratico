<?php

include "conexao.php";

?>

<?php

	$id = $_GET['id'];		
	$sql = mysqli_query($conexao, "DELETE FROM funcionario WHERE id='$id'");	
	
	header("Location: listarFuncionario.php");

?>