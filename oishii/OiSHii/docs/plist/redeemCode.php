<?php


//header("Content-Type: application/xml; charset=utf-8");
echo '<?xml version="1.0" encoding="utf-8"?>
      <!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">';
	  
	  /* Include & Initialize Mage */
		require_once('../app/Mage.php');
		umask(0);
		Mage::App('default');
		$dataError = true;
		$validationError = true;
		$loginError = true;
		$couponError = false;
		$productError = false;
		$dishError = true;
		$couponCode = strip_tags($_REQUEST['couponcode']);
		$shopping_cart[] = $_REQUEST['shopping_cart'];
		$coupon = Mage::getModel('salesrule/rule');
	  $couponCollection = $coupon->getCollection();
	  if($couponCode)
	  {
		foreach($couponCollection as $c)
		{
			if($c->getCode() == $couponcode)
			{
				$discount =  $c->getDiscountAmount();
				
			}
		}
	
		if(!$discount)
		{ 
			$couponError = true;
		}
	 }
	 
	  //************validation**********//
	if (isset($_REQUEST['mac']) && isset($_REQUEST['sid']) && isset($_REQUEST['shopping_cart'])) 
	 {
	if (!empty($_REQUEST['mac']) && !empty($_REQUEST['sid']) && !empty($_REQUEST['shopping_cart']))
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
				/*$items[0]['productid'] = 8;
				$items[0]['quanity'] = 3;
				$items[1]['productid'] = 9;
				$items[1]['quanity'] = 1;*/
				$flag=0;
				$subtotal = 0;
						foreach($shopping_cart as $item)
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
								$price = Mage::getModel('catalog/product')->load($item['productid'])->getPrice()*$item['quanity'];
								$shopping_cart[] = array("ProductId" => $item['productid'], "Quantity" => $item['quanity'],"Price" => $price);
								$subtotal = $subtotal+$price;
							}else{
							
								$productError = true;
							}
						}
								
						if($discount)
						 {
							$rate = ($subtotal*$discount)/100;
							$grandTotal = $subtotal - $rate;
						 }else {
						 
							$grandTotal = $subtotal;
						 }
					
						if($flag == 0)
						{	
							$dishError = false;
						}
					 
			
			  }
	  
		} 
	
	}
	

/* Generate Plist */

if (!$dataError) {
	if (!$loginError) {
		if (!$couponError) {
			if (!$productError){
									if($dishError) {
											$plistContent = '<plist version="1.0">';
											foreach ($shopping_cart as $part) {
											$plistContent.='<dict>
													<key>Productid</key>
													<real>'.$part['ProductId'].'</real>
													<key>Quanity</key>
													<real>'.$part['Quantity'].'</real>
													<key>Price</key>
													<real>'.$part['Price'].'</real>
												</dict>'; }
											$plistContent.='<key>Total</key>
													<string>Amount</string>';
	
											$plistContent.='<dict>
															<key>subtotal</key>
															<real>'.$subtotal.'</real>
															<key>discount</key>
															<real>'.$discount.'</real>
															<key>grandtotal</key>
															<real>'.$grandtotal.'</real>

															</dict>';
												
											$plistContent .= '</plist>';	
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
				<string>Invalid Coupon Code</string>
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
