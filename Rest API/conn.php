<?php
$db_user = "scott";
$db_pass = "1234"; 
$con = oci_connect($db_user,$db_pass,'(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = ARSHAD)(PORT = 1521)) (CONNECT_DATA = (SERVICE_NAME = orcl) (SID = orcl)))');
   if($con) 
     { 
		//echo "Oracle Connection Successful."."<br>"; 
	 } 
   else 
	 { 
		//die('Could not connect to Oracle Arshad: '); 
	 }
//oci_close($con);
?>