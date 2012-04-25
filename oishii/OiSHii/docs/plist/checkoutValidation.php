<?php


header("Content-Type: application/xml; charset=utf-8");
echo '<?xml version="1.0" encoding="utf-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">';
	  
	  /* Include & Initialize Mage */
		require_once('../app/Mage.php');
		umask(0);
		Mage::App('default');
		$dataError = true;
		$validationError = true;
		$loginError = true;
		$productError = false;
		$dishError = true;
	

	  
	  $couponCode = strip_tags($_REQUEST['couponcode']);
	  $billingId = strip_tags($_REQUEST['billingId']);
	  $shippingId = strip_tags($_REQUEST['shippingId']);
	  $deliverytime = strip_tags($_REQUEST['deliverytime']);
	  $token = strip_tags($_REQUEST['token']);
	  $savedtoken = strip_tags($_REQUEST['savedtoken']);
	  $is_saved_cc = strip_tags($_REQUEST['is_saved_cc']);
	  
	 
	  //************validation**********//
	if (isset($_REQUEST['mac']) && isset($_REQUEST['sid']) && isset($_REQUEST['billingId']) && isset($_REQUEST['shippingId']) && isset($_REQUEST['deliverytime']) && isset($_REQUEST['token'])) 
	 {
	if (!empty($_REQUEST['mac']) && !empty($_REQUEST['sid']) && !empty($_REQUEST['billingId']) && !empty($_REQUEST['shippingId']) && !empty($_REQUEST['deliverytime']))
		{  
			$dataError = false;
			$shopping_cart = array();
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
				
				$validator = Mage::getModel('checkout/validation');
				
			

					
					if ($validator->chkPostcode($shippingId,$cusId)=="success" && !$validator->deliveryAppTimeValidation($deliverytime))
					{
						
						$validationError = false;
						$items[0]['productid'] = 8;
						$items[0]['quanity'] = 3;
						$flag=0;
						foreach($items as $item)
						{	
							
							//Validating Category Wise -starts --//
								$categories = Mage::getModel('catalog/product')->load($item['productid'])->getCategoryIds(); 
								foreach($categories as $k => $_category_id) 
								{
					 
											$_category = Mage::getModel('catalog/category')->load($_category_id); 
						 
											if ($_category) 
											{
														while($_category->getLevel() != 2) 
														{
											
															$_category = $_category->getParentCategory();
					
															if (!$_category) {
															break;
															}
														}
					
												if ($_category->getId() == 'Main Dishes' || $_category->getId() == '23' ) 
												{
													
													$flag = 1;
													break;
												}
						
											} 
								}
							//Validating Category Wise -Ends --//
							
							$stockitem = Mage::getModel('cataloginventory/stock_item')->loadByProduct($item['productid']);
							$oldQty = $stockitem->getQty();
							if($oldQty >= $item['quanity'])
							{						
								$shopping_cart[] = array("ProductId" => $item['productid'], "Quantity" => $item['quanity']);
							}else{
							
								$productError = true;
							}
						}
						 
						if($flag == 0)
						{	
							$dishError = false;
						}
					}  
			
			  }
	  
		} 
	
	}

/* Generate Plist */

if (!$dataError) {
	if (!$loginError) {
		if (!$validationError) {
			if (!$productError){
									if($dishError) {
											$plistContent = '<plist version="1.0">
												<dict>
													<key>success</key>
													<true/>
													<key>message</key>
													<string>Order Validated Successfully</string>
												</dict>
											</plist>';
										}else{
										
											$plistContent = '<plist version="1.0">
										<dict>
											<key>success</key>
											<false/>
											<key>message</key>
											<string>Maindish is Required</string>
										</dict>
									</plist>';
										
										}
								}
			else{
			
				$plistContent = '<plist version="1.0">
			<dict>
				<key>success</key>
				<false/>
				<key>message</key>
				<string>Request Quanity is Not Available</string>
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
				<string>Invalid Fields Entered</string>
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
