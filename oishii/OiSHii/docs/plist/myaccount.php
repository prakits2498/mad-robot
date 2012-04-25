<?php
header("Content-type: text/xml"); 
require_once('../app/Mage.php');
Mage::App('default');

/* Get User MAC ID and User Login Credentials */
$mobileMacId = strip_tags(addslashes($_REQUEST['mac']));
$sessionId = strip_tags($_REQUEST['sid']);

$cusId = '';
	
	/* Check if MAC Id and Customer Exist */
	$dbHandle = Mage::getSingleton('core/resource')->getConnection('core_write');
	$query = "SELECT * FROM mobile_session WHERE mobile_customer_session_id='".$sessionId."' and mobile_mac_id='".$mobileMacId."'";
	$userCheck = $dbHandle->query($query);
	$userRow = $userCheck->fetchAll();
	foreach($userRow as $data)
	{
		$cusId = $data['mobile_customer_id'];
	}

	
$xml_output = '<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN"
"http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
  <array>';
  
if($cusId)
{	
	$checkoutSession = Mage::getSingleton('core/session');
	$checkoutSession->setSessionId($sessionId); 
	Mage::getSingleton('customer/session')->setCustomerId($cusId);

	$customer = Mage::getModel('customer/customer')->load($cusId)->getData();
	
	$subscriber = Mage::getModel('newsletter/subscriber')->loadByEmail($customer['email']);
       
	$xml_output .= "<dict>\n";
	$xml_output .= "<key>details</key>\n";
	$xml_output .= "<dict>
									  <key>title</key>
									  <string>".$customer['prefix']."</string>
									  <key>firstname</key>
									  <string>".$customer['firstname']."</string>
									  <key>lastname</key>
									  <string>".$customer['lastname']."</string>
									  <key>email</key>
									  <string>".$customer['email']."</string>
									  <key>subscribed</key>
									  <integer>".$subscriber->getStatus()."</integer>
									  <key>address</key>
									  <array>\n";
									  
	$customer = Mage::getModel('customer/customer')->load($cusId); //put customer id here
	$data = array();
	$address = $customer->getAddressesCollection();
	$billing = $customer->getDefaultBilling();
	$shipping = $customer->getDefaultShipping();	
	foreach ($address as $add)
	{
		        $dBilling = 0;
				$dShipping = 0;
				if($add->getId()==$billing)
				{
					$dBilling = 1;
				}
				if($add->getId()==$shipping)
				{
					$dShipping = 1;
				}
				$street = $add->getStreet();
				$xml_output .=	"<dict>
									  <key>id</key>
									  <integer>".$add->getId()."</integer>
									  <key>company</key>
									  <string>".$add->getCompany()."</string>
									  <key>floor</key>
									  <string>".$street[0]."</string>
									  <key>address</key>
									  <string>".$street[1]."</string>
									  <key>city</key>
									  <string>".$add->getCity()."</string>
									  <key>postcode</key>
									  <string>".$add->getPostcode()."</string>
									  <key>mobile</key>
									  <string>".$add->getTelephone()."</string>
									  <key>shipping</key>
									  <string>".$dShipping."</string>
									  <key>billing</key>
									  <string>".$dBilling."</string>
									</dict>";
	}
	
	$xml_output .= "</array>\n";
	
	$savedCC = Mage::getModel('sagepayserver/standard'); 
	$res  = $savedCC->getSaveTokens();
	$xml_output .= "<key>cc</key>
					<array>\n";
	foreach($res as $value)
	{	
		$xml_output .=	"<dict>
									  <key>token</key>
									  <string>".$value['token']."</string>
									  <key>type</key>
									  <string>".$value['card_type']."</string>
									  <key>number</key>
									  <string>XXXX XXXX XXXX ".$value['last_four']."</string>									  
									</dict>";
	}
	$xml_output .= '</array>';	
	$xml_output .= "</dict>";	
	$xml_output .= "</dict>\n";	
}
else
{
		$xml_output .= '<dict>
					<key>success</key>
					<false/>
					<key>message</key>
					<string>Invalid Login Details</string>
			  </dict>';
}

$xml_output .= '</array>
	</plist>  
	';
echo $xml_output;