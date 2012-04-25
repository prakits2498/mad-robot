<?php header("Content-Type: application/xml; charset=utf-8");
echo '<?xml version="1.0" encoding="utf-8"?>
	<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
	<plist version="1.0">';
/* Get User MAC ID and User Login Credentials */
$sessionId = strip_tags(addslashes($_REQUEST['sid']));
$macAd = strip_tags($_REQUEST['mac']);


/* Include & Initialize Mage */
require_once('../app/Mage.php');
umask(0);
Mage::App('default');

$addressData['company'] = strip_tags($_POST['comp']);
$addressData['city'] = strip_tags($_POST['city']);
$addressData['street'][] = strip_tags($_POST['floor']);
$addressData['street'][] = strip_tags($_POST['address']);
$addressData['postcode'] = strip_tags($_POST['postcode']);
$addressData['telephone'] = strip_tags($_POST['telephone']);

if (isset($_POST['billing'])) {
	$addressData['default_billing'] = '1';
}
if (isset($_POST['shipping'])) {
	$addressData['default_shipping'] = '1';
}

$addLocError = true; 
$loginError = true; 
/* If DATA is available */
if (isset($_REQUEST['mac']) && isset($_REQUEST['sid'])) {
	if (!empty($_REQUEST['mac']) && !empty($_REQUEST['sid'])) {
		$loginError = false; 

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

			$customer = Mage::getModel('customer/customer')->load($customerId); 
			$addressData['prefix'] = $customer->title;
			$addressData['firstname'] = $customer->firstname;
			$addressData['lastname'] = $customer->lastname;
		
		        $address  = Mage::getModel('customer/address');
		        $errors = array();
 
			/* @var $addressForm Mage_Customer_Model_Form */
		        $addressForm = Mage::getModel('customer/form');
		        $addressForm->setFormCode('customer_address_edit')
				->setEntity($address);
		

			$validation = Mage::getModel('customer/validation'); 
			$addressErrors  = $validation->customerAddressValidation($addressData);
		        if ($addressErrors !== true) {
        			$errors = $addressErrors;
			}

		        try {
				$addressForm->compactData($addressData);
				$address->setPrefix($addressData['prefix']);
                		$address->setCustomerId($customerId)
		                    ->setIsDefaultBilling($addressData['default_billing'])
                		    ->setIsDefaultShipping($addressData['default_shipping']);

		
                		if ($addressErrors !== true) {
		                    $errors = array_merge($errors, $addressErrors);
                		}

				if (count($errors) === 0) {
					$address->save();
		                    	$addLocError= false; 	
				} 
			} catch (Mage_Core_Exception $e) {
				$addLocError= true; 	
			} catch (Exception $e) {
				$addLocError= true; 		
			}
		}
	}
}
$plistContent = "";
if ($loginError) { 
	$plistContent .= '<dict>
				<key>success</key>
				<false />
				<key>message</key>
				<string>User Not Logged In</string>
			</dict>';

} else { 
	if ($addLocError) {
		echo "1";
		$plistContent .= '
			<key>success</key>
			<false />
			<key>messages</key>
			<array>';

		//echo "boo";
		//print_r($errors);
		$errorString = implode("\n", $errors);
		//echo $errorString; 
		if (preg_match('/ADDRESS_POSTCODE_VALID_FAILED/', $errorString)) {
			$plistContent .= '
				<dict>
					<key>message</key>
					<string>Invalid Post Code</string>
				</dict>';
		}
		if (preg_match('/ADDRESS_CITY_FAILED/', $errorString)) {
			$plistContent .= '
				<dict>
					<key>message</key>
					<string>Empty Field - City</string>
				</dict>';
		} 
		if (preg_match('/ADDRESS_FLOOR_FAILED/', $errorString)) {
			$plistContent .= '
				<dict>
					<key>message</key>
					<string>Empty Field - Floor</string>
				</dict>';
		}
		if (preg_match('/ADDRESS_ADDRESS_FAILED/', $errorString)) {
			$plistContent .= '
				<dict>
					<key>message</key>
					<string>Empty Field - Address</string>
				</dict>';
		}
		if (preg_match('/ADDRESS_TELEPHONE_FAILED/', $errorString)) {
			$plistContent .= '
				<dict>
					<key>message</key>
					<string>Empty Field - Telephone</string>
				</dict>';
		}
		if (preg_match('/ADDRESS_POSTCODE_FAILED/', $errorString)) {
			$plistContent .= '
				<dict>
					<key>message</key>
					<string>Empty Field - Post Code</string>
				</dict>';
		}
		$plistContent .='</array>';
	} else {
		$plistContent .= '
			<dict>
				<key>success</key>
				<true />
				<key>message</key>
				<string>Location Succesfully Added</string>
			</dict>';
	}
} 

$plistContent .= '</plist>';

echo trim($plistContent);

?>
