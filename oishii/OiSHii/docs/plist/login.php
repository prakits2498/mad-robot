<?php
header("Content-Type: application/xml; charset=utf-8");

/* Get User MAC ID and User Login Credentials */
$mobileMacId = strip_tags(addslashes($_REQUEST['mac']));
$userLogin = strip_tags($_REQUEST['login']);
$userPassword = strip_tags($_REQUEST['pswd']);

/* Include & Initialize Mage */
require_once('../app/Mage.php');
umask(0);
Mage::App('default');
$loginError = true; 
$dataError = true;
/* If DATA is available */
if (isset($_REQUEST['mac']) && isset($_REQUEST['login']) && isset($_REQUEST['pswd'])) {
	$dataError = false; 
	/* Perform Mage Login and Get user ID */
	session_start();
	$websiteId = Mage::app()->getWebsite()->getId();
	$storeName = Mage::app()->getStore();

	$customerHandle = Mage::getModel('customer/customer');
	$customerHandle->website_id = $websiteId;
	$customerHandle->setStore($storeName);

	$sessionHandle = Mage::getSingleton('customer/session');
	try {
		$sessionHandle->login($userLogin, $userPassword);
		$customerHandle->loadByEmail($userLogin);
		$loginError = false;
		$customerId = $sessionHandle->isLoggedIn() ? $sessionHandle->getCustomer()->getId() : false;
		$sessionId = $sessionHandle->isLoggedIn() ? session_id() : false;
	} catch(Exception $e) {
		$loginError = true;
	}
} 


/* If Logged In, Enter data into new table */
if (!$loginError) {
	$dbHandle = Mage::getSingleton('core/resource')->getConnection('core_write');
	
	/* Check if MAC Id and Customer Exist */
	$userCheck = $dbHandle->query("SELECT * FROM mobile_session WHERE 
		mobile_customer_id='".$customerId."'");
		
	if ($userRow = $userCheck->fetch()) {
		$userQuery = "UPDATE mobile_session SET 
		mobile_mac_id='".$mobileMacId."',
		mobile_customer_session_id='".$sessionId."' 
		WHERE mobile_customer_id='".$customerId."'";
	} else {
		$userQuery = "INSERT INTO mobile_session (mobile_customer_id, mobile_mac_id, mobile_customer_session_id) VALUES (
		'".$customerId."',
		'".$mobileMacId."',
		'".$sessionId."')";
	}
	$dbHandle->query($userQuery);
}

/* Generate Plist */
if (!$dataError) {
	if (!$loginError) {
		$plistContent = '<?xml version="1.0" encoding="utf-8"?>
			<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
			<plist version="1.0">
		    		<dict>
      					<key>sessionId</key>
					<string>'.$sessionId.'</string>
					<key>customerId</key>
					<integer>'.$customerId.'</integer>
					<key>message</key>
					<string>Login Successful</string>
				</dict>
			</plist>';
	} else {
		$plistContent = '<?xml version="1.0" encoding="utf-8"?>
			<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
			<plist version="1.0">
	    			<dict>
      					<key>sessionId</key>
					<string></string>
					<key>customerId</key>
					<integer>0</integer>
					<key>message</key>
					<string>Invalid Username or Password</string>
				</dict>
			</plist>';
	}
} else {
	$plistContent = '<?xml version="1.0" encoding="utf-8"?>
		<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
		<plist version="1.0">
	    		<dict>
      				<key>sessionId</key>
				<string></string>
				<key>customerId</key>
				<integer>0</integer>
				<key>message</key>
				<string>Insufficient Data Supplied</string>
			</dict>
		</plist>';
}


echo $plistContent; 
?>



