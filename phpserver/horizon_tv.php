<?php
        // create curl resource
        $ch = curl_init();
        $agent = 'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705; .NET CLR 1.1.4322)';
        $date = $_GET['date'];
	      $subpage = $_GET['subpage'];
        $country = $_GET['country'];        

        // epg grabbing enable only 1 $url line below // disables line
  $url = "https://web-api.horizon.tv/oesp/api/" . $country . "/web/programschedules/" . $date . "/" . $subpage . "";
	// $url = "https://www.yeloplay.be/api/pubba/v1/events/schedule-day/outformat/json/lng/en/channel/" . $channel . "/day/" . $date . "/platform/web/";
  // url_index{url|https://web-api.horizon.tv/oesp/api/##country##/web/programschedules/|urldate|/|subpage|}

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
