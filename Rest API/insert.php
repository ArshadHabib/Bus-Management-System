<?php
error_reporting(E_ALL & ~E_NOTICE & ~E_WARNING);
include "conn.php";
$response=array();

if(isset($_POST["bus_id"],$_POST["bus_departure"],$_POST["bus_destination"],$_POST["bus_date"],$_POST["bus_seats"]))
{
		$bus_id=$_POST["bus_id"];
		$bus_departure=$_POST["bus_departure"];
		$bus_destination=$_POST["bus_destination"];
		$bus_date=$_POST["bus_date"];
		$bus_seats=$_POST["bus_seats"];
		$bus_insert_q="INSERT INTO add_bus VALUES($bus_id,'$bus_departure','$bus_destination','$bus_date',$bus_seats)";
		$bus_insert_q_id = oci_parse($con, $bus_insert_q); 		
		$bus_insert_q_r = oci_execute($bus_insert_q_id);		
		if($bus_insert_q_r)
		{
			//echo "Record Inserted Successfully"."<br>";
			$response['id']=$bus_id;
			$response['reqmsg']="Inserted Successfully";
			$response['reqcode']="1";
		}
		else
		{
			//echo "Record Insertion Failed"."<br>";
			$response['id']="NA";
			$response['reqmsg']="Bus Insertion Failed";
			$response['reqcode']="2";
		}	
}
else
{
	$response['id']="NA";
	$response['reqmsg']="Incomplete Request!";
	$response['reqcode']="3";
}
$x=json_encode($response);
echo $x;
oci_close($con);

if($con)
{
	//echo "Oracle Connection Closed";
}
?>