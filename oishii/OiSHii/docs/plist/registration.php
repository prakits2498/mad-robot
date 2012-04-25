<?php //header("Content-Type: application/xml; charset=utf-8");
echo '<?xml version="1.0" encoding="utf-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">';
/* Get User MAC ID and User Login Credentials */
$userTitle = strip_tags(addslashes($_REQUEST['title']));
$userFirstName = strip_tags($_REQUEST['fname']);
$userLastName = strip_tags($_REQUEST['lname']);
$userEmail = strip_tags($_REQUEST['email']);
$userPassword = strip_tags($_REQUEST['pswd']);
$is_subscribed = strip_tags($_REQUEST['is_subscribed']);
if($is_subscribed != 1)
{
	$is_subscribed = 0;
}


/* Include & Initialize Mage */
require_once('../app/Mage.php');
umask(0);
Mage::App('default');
$regError = true; 
$dataError = true;
$dupReg = false; 
$validationError = true; 
/* If DATA is available */
if (isset($_REQUEST['title']) && isset($_REQUEST['fname']) && isset($_REQUEST['lname']) && isset($_REQUEST['email']) && isset($_REQUEST['pswd'])) {
	if (!empty($_REQUEST['title']) &&  !empty($_REQUEST['fname']) && !empty($_REQUEST['lname']) && !empty($_REQUEST['email']) && !empty($_REQUEST['pswd'])) {
		$dataError = false; 
		/* Perform Mage Login and Get user ID */
		session_start();
		$websiteId = Mage::app()->getWebsite()->getId();
		$storeName = Mage::app()->getStore();

		$customerHandle = Mage::getModel('customer/customer');
		$customerHandle->website_id = $websiteId;
		$customerHandle->setStore($storeName);

		/* First Step is to see if a valid email has been entered */
		$validator = Mage::getModel('checkout/validation');
		if ($validator->isEmail($userEmail) && $validator->isAlpha($userTitle) && $validator->isAlpha($userFirstName) && $validator->isAlpha($userLastName)) {
			$validationError = false;
			try {
				/* If New Customer, Save info */
				$customerHandle->setIsSubscribed(1); 
				$customerHandle->firstname = $userFirstName;
				$customerHandle->lastname = $userLastName;
				$customerHandle->email = $userEmail;
				$customerHandle->password_hash = md5($userPassword);
				$customerHandle->title = $userTitle;
				$customerHandle->setIsSubscribed(1); 
					//echo $is_subscribed; die("s");
				if ($customerHandle->save()) {
					$customerHandle->setConfirmation(null);
					$customerHandle->setStatus(1);
					$customerHandle->sendNewAccountEmail($type = 'confirmed', $backUrl = '', $storeId = '0');
					//$customerHandle->setIsSubscribed(1); 
					$customerHandle->save();
					$regError = false;
				}
			} catch(Exception $e) {
				$regError = true;
				/* If customer already exists, mark dup registration */
				if(preg_match('/Customer email already exists/', $e)){
	        			$dupReg = true; 
				}
			}	
		} 
	}
}	
/* Generate Plist */
if (!$dataError) {
	if (!$validationError) {
		if (!$regError && !$dupReg) {
			$plistContent = '<plist version="1.0">
				<dict>
					<key>success</key>
					<true/>
					<key>message</key>
					<string>Registration Successful</string>
				</dict>
			</plist>';
		} else {
			$plistContent = '<plist version="1.0">
				<dict>
					<key>success</key>
					<false/>
					<key>message</key>
					<string>Duplicate Registration Attempted</string>
				</dict>
			</plist>';
		}
	} else {
		$plistContent = '<plist version="1.0">
			<dict>
				<key>success</key>
				<false/>
				<key>message</key>
				<string>Invalid Fields Entered</string>
			</dict>
		</plist>';
	}
} else {
	$plistContent = '<plist version="1.0">
		<dict>
			<key>success</key>
			<false/>
			<key>message</key>
			<string>Insufficient Data Supplied</string>
		</dict>
	</plist>';
}


echo trim($plistContent); 
?>



 
