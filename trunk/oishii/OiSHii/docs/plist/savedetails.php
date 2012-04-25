<?php

header("Content-Type: application/xml; charset=utf-8");
echo '<?xml version="1.0" encoding="utf-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">';
	  
/* Get User MAC ID and User Login Credentials */
$sessionId = strip_tags(addslashes($_REQUEST['sid']));
$macAd = strip_tags($_REQUEST['mac']);
$title = strip_tags($_REQUEST['title']);
$firstname = strip_tags($_REQUEST['firstname']);
$lastname = strip_tags($_REQUEST['lastname']);
$email = strip_tags($_REQUEST['email']);
$is_subcribed = strip_tags($_REQUEST['is_subcribed']);



/* Include & Initialize Mage */
require_once('../app/Mage.php');
umask(0);
Mage::App('default');
$dataError = true;
$loginError = true;
$emailError = true; 
$validationError = true;
if (isset($_REQUEST['mac']) && isset($_REQUEST['sid']) && isset($_REQUEST['firstname']) && isset($_REQUEST['lastname']) && isset($_REQUEST['title']) && isset($_REQUEST['email'])) 
	 {	
	if (!empty($_REQUEST['mac']) && !empty($_REQUEST['sid']) && !empty($_REQUEST['firstname']) && !empty($_REQUEST['lastname']) && !empty($_REQUEST['title']) && !empty($_REQUEST['email'])) 
		{
			
			$dataError = false; 
			$websiteId = Mage::app()->getWebsite()->getId();
			
			$customer = Mage::getModel('customer/customer');

			if ($websiteId) 
			{
				$customer->setWebsiteId($websiteId);
			}
			$customer->loadByEmail($email);
			
			$dbHandle = Mage::getSingleton('core/resource')->getConnection('core_write');
						/* Check if User Mobile Session Exist */
						$userCheck = $dbHandle->query("SELECT * FROM mobile_session 
						WHERE mobile_mac_id='".$macAd."'
						AND mobile_customer_session_id='".$sessionId."'"
						);
		if ($userRow = $userCheck->fetch()) {
			$loginError = false; 
			$customerId = $userRow['mobile_customer_id'];
			/* Perform Mage Save for password*/
			$websiteId = Mage::app()->getWebsite()->getId();
			$storeName = Mage::app()->getStore();
			$customerData = Mage::getModel('customer/customer')->load($customerId);
			if (!$customer->getId() || ($customer->getId() == $customerId)) 
			 {	
				$emailError = false;
				
				/* Get User Details from Session Id and Mac Add */
				$validator = Mage::getModel('checkout/validation');
				
				if ($validator->isEmail($email) && $validator->isAlpha($title) && $validator->isAlpha($firstname) && $validator->isAlpha($lastname)) {
						
							$validationError = false; 
							if($customer)
							{
								
											$customer->prefix = $title;
											$customer->firstname = $firstname;
											$customer->lastname = $lastname;
											$customer->email = $email;
											$customer->is_subscribed = $is_subcribed;
											$customer->save();	
							}
				}	
			}
		}
	  }	
	}

/* Generate Plist */
if (!$dataError) {
	if (!$loginError) {
		if (!$emailError) {
				if (!$validationError) {
				$plistContent = '<plist version="1.0">
					<dict>
						<key>success</key>
						<true/>
						<key>message</key>
						<string>Details Changed Successfully</string>
					</dict>
				</plist>';
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
		
		}else {
				$plistContent = '<plist version="1.0">
					<dict>
						<key>success</key>
						<false/>
						<key>message</key>
						<string>Email already exist</string>
					</dict>
				</plist>';
			}
		} 
		else {
			$plistContent = '<plist version="1.0">
				<dict>
					<key>success</key>
					<false/>
					<key>message</key>
					<string>User Has To Be Logged On Before Changing Details</string>
				</dict>
			</plist>';
		}
	}	
else {
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