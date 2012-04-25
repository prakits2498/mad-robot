<?php

header("Content-Type: application/xml; charset=utf-8");
echo '<?xml version="1.0" encoding="utf-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">';
	  
	  /* Include & Initialize Mage */
		require_once('../app/Mage.php');
		umask(0);
		Mage::App('default');
		$dataError = true;
		$tokenError = true;
		$loginError = true;
		$token = strip_tags($_REQUEST['token']);
	
	  
	 
	
	  //************validation**********//
	if (isset($_REQUEST['mac']) && isset($_REQUEST['sid']) && isset($_REQUEST['token'])) 
	 {
	if (!empty($_REQUEST['mac']) && !empty($_REQUEST['sid']) && !empty($_REQUEST['token']))
		{  
			$dataError = false;
			
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
		
		
		if($cusId!='')
			{	
				
				$loginError = false;
				$query = "select * from sagepayserver_tokencard where customer_id=".$cusId." and token='".$token."'";
				$data = Mage::getSingleton('core/resource')->getConnection('core_read')->fetchAll($query); 	
		if(sizeof($data)==1)
		{	
				$config = Mage::getModel('sagepayserver/config');
				$getRequest = Mage::getModel('sagepayserver/standard');
				$payment = Mage::getSingleton('sagepayserver/standard');
				
				$strProtocol = $config->getVersion();
				$strVendorName = $config->getVendorName();
				$strPost= "VPSProtocol=" . $strProtocol;
				$strPost=$strPost . "&TxType=REMOVETOKEN"; //PAYMENT by default.  You can change this in the includes file
				$strPost=$strPost . "&Vendor=" . $strVendorName;
				$strPost=$strPost . "&Token=".$token; 				

							
				$arrResponse = $getRequest->getrequestPost($payment->getRemoveTokenURL(), $strPost);
				
				
				/* Analyse the response from Sage Pay Server to check that everything is okay
				** Registration results come back in the Status and StatusDetail fields */
				$strStatus=$arrResponse["Status"];
				$strStatusDetail=$arrResponse["StatusDetail"];
				
				
				if($strStatus=='OK')
				{
					$query = " delete from sagepayserver_tokencard  where customer_id=".$cusId." and token='".$token."'";
					$connection = Mage::getSingleton('core/resource')->getConnection('core_write');
					$connection->query($query);	
					$tokenError = false;	
				}
		}		
				  
			
			  }
	  
		} 
	
	}

/* Generate Plist */

if (!$dataError) {
	if (!$loginError) {
		if (!$tokenError) {
				$plistContent = '<plist version="1.0">
					<dict>
						<key>success</key>
						<true/>
						<key>message</key>
						<string>Token Deleted Successfully</string>
					</dict>
				</plist>';
			} else {
	
			$plistContent = '<plist version="1.0">
			<dict>
				<key>success</key>
				<false/>
				<key>message</key>
				<string>Invalid Token Entered</string>
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
