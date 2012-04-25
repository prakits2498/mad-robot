<?php header("Content-Type: application/xml; charset=utf-8");
echo '<?xml version="1.0" encoding="utf-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">';
/* Get User MAC ID and User Login Credentials */
$sessionId = strip_tags(addslashes($_REQUEST['sid']));
$macAd = strip_tags($_REQUEST['mac']);
$newPassword = strip_tags($_REQUEST['npswd']);
$currPassword = strip_tags($_REQUEST['cpswd']);

/* Include & Initialize Mage */
require_once('../app/Mage.php');
umask(0);
Mage::App('default');
$dataError = true;
$loginError = true; 
$pswdError = true; 
$cpswdError = true; 
/* If DATA is available */
if (isset($_REQUEST['mac']) && isset($_REQUEST['sid']) && isset($_REQUEST['npswd']) && isset($_REQUEST['cpswd'])) {
	if (!empty($_REQUEST['mac']) && !empty($_REQUEST['sid']) && !empty($_REQUEST['npswd']) && !empty($_REQUEST['cpswd'])) {
		$dataError = false; 

		/* Get User Details from Session Id and Mac Add */
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

			$customerHandle = Mage::getModel('customer/customer')->load($customerId); 
			if ($customerHandle->password_hash == md5($currPassword)) {
				$cpswdError = false; 
				/* First Step is to see if a valid email has been entered */
				try {
					$customerHandle->password_hash = md5($newPassword);
					if ($customerHandle->save()) {
						$pswdError = false; 
					}
				} catch(Exception $e) {
					$pswdError = true;
				}
			}
			
		} 
	}
}	
/* Generate Plist */
if (!$dataError) {
	if (!$loginError) {
		if (!$cpswdError) {
			if (!$pswdError) {
				$plistContent = '<plist version="1.0">
					<dict>
						<key>success</key>
						<true/>
						<key>message</key>
						<string>Password Change Successful</string>
					</dict>
				</plist>';
			} else {
				$plistContent = '<plist version="1.0">
					<dict>
						<key>success</key>
						<false/>
						<key>message</key>
						<string>Password Change Failed</string>
					</dict>
				</plist>';
			}
		} else {
			$plistContent = '<plist version="1.0">
				<dict>
					<key>success</key>
					<false/>
					<key>message</key>
					<string>Current Password Mismatch</string>
				</dict>
			</plist>';
		}
	} else {
		$plistContent = '<plist version="1.0">
			<dict>
				<key>success</key>
				<false/>
				<key>message</key>
				<string>User Has To Be Logged On Before Changing Password</string>
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



 
 
