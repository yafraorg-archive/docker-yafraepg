<?php
        // create curl resource
        $ch = curl_init();
        $agent = 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0';
        $eventid = $_GET['eventid'];
       
	$url = "https://web-api.horizon.tv/oesp/api/" . $eventid . "/";

        //return the transfer as a string
	curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_USERAGENT, $agent);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        
        // $output contains the output string
        $output = curl_exec($ch);

        // close curl resource to free up system resources
        curl_close($ch);

        echo $output;

?>
