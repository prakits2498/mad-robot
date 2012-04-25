<?php
header("Content-type: text/xml"); 
require_once('../app/Mage.php');
Mage::App('default');

$postcodes = Mage::getModel('sushipostcode/postcode')->fetchPostcode();

$xml_output = '<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN"
"http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
  <array>';
	foreach($postcodes as $key => $val)
	{
		$xml_output .= '<dict>
		     <key>postcode</key>
			<string>'.$val.'</string>
		</dict>';
	}
$xml_output .= '</array>
</plist>  
';
echo $xml_output;