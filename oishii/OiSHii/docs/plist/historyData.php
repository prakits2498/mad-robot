<?php
header("Content-type: text/xml"); 
require_once('../app/Mage.php');
Mage::App('default');
$xml_output = "<?xml version=\"1.0\"?>\n";
	$xml_output .= "<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"
\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">";
	$xml_output .= "<plist version=\"1.0\">\n";
	$xml_output .= "<array>\n";			


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
	$checkoutSession = Mage::getSingleton('core/session');
	$checkoutSession->setSessionId($sessionId);

	$orders = Mage::getResourceModel('sales/order_collection')
        ->addAttributeToSelect('*')
		->addAttributeToFilter('customer_id', $cusId )
        ->load();
		
				
	        $xml_output .= "<dict>\n";
			$xml_output .=	"<key>groups</key>\n";
			$xml_output .=	"<string>Todays orders</string>\n";
			$xml_output .=	"<key>orders</key>\n";
			$xml_output .= "<array>\n";
			foreach($orders as $order) { 
				
				$cur_date = $order->getCreatedAt();
				$todays_date = date("Y-m-d"); 
				$today = strtotime($todays_date); 
				$expiration_date = strtotime($cur_date);
				
				if ($expiration_date > $today) 
				{
					$deliverytime = Mage::getModel('sushisales/sales')->deliverytimeval($order->getIncrementId());
					$getorder = Mage::getModel('sales/order')->load($order->getId());
					$xml_output .= "<dict>\n";
					$xml_output .=	"<key>orderid</key>\n";
					$xml_output .=	"<integer>".$order->getId()."</integer>\n";
					$xml_output .=	"<key>deliverytime</key>\n";
					$xml_output .=	"<string>".$deliverytime."</string>\n";
					$xml_output .=	"<key>discount</key>\n";
					$xml_output .=	"<real>".$order->getDiscountAmount()."</real>\n";
					$xml_output .=	"<key>subtotal</key>\n";
					$xml_output .=	"<real>".$order->getBaseSubtotal()."</real>\n";
					$xml_output .=	"<key>totalprice</key>\n";
					$xml_output .=	"<real>".$order->getGrandTotal()."</real>\n";
					$xml_output .=	"<key>status</key>\n";
					$xml_output .=	"<string>".$order->getStatus()."</string>\n";
					$xml_output .=	"<key>date</key>\n";
					$xml_output .=	"<date>".str_replace(" ","T",$order->getCreatedAt())."Z</date>\n";
					$items = $getorder->getAllItems();
					$itemcount=count($items);
					$xml_output .=	"<key>items</key>\n";
					$xml_output .= "<array>\n";
				
					foreach ($items as $itemId => $item)
					{	
						$xml_output .= "<dict>\n";
						$xml_output .=	"<key>id</key>\n";
						$xml_output .=	"<integer>".$item->getProductId()."</integer>\n";	
						$xml_output .=	"<key>name</key>\n";
						$xml_output .=	"<string>".$item->getName()."</string>\n";
						$xml_output .=	"<key>price</key>\n";
						$xml_output .=	"<real>".$item->getPrice()."</real>\n";						
						$xml_output .=	"<key>sku</key>\n";
						$xml_output .=	"<string>".$item->getSku()."</string>\n";		
						$xml_output .=	"<key>quantity</key>\n";
						$xml_output .=	"<real>".$item->getQtyOrdered()."</real>\n";	
						$xml_output .=	"</dict>";
					}
					
					
					$xml_output .= "</array>\n";
					$xml_output .=	"</dict>";												
					}					
				}
				
				$xml_output .= "</array>\n";
				$xml_output .=	"</dict>";	
				
				
			    $xml_output .= "<dict>\n";
				$xml_output .=	"<key>groups</key>\n";
				$xml_output .=	"<string>past orders</string>\n";
				$xml_output .=	"<key>orders</key>\n";
				$xml_output .= "<array>\n";
				foreach($orders as $order) 
				{ 
				
					$cur_date = $order->getCreatedAt();
					$todays_date = date("Y-m-d"); 
					$today = strtotime($todays_date); 
					$expiration_date = strtotime($cur_date);
					
					if ($expiration_date < $today) 
					{					
						$deliverytime = Mage::getModel('sushisales/sales')->deliverytimeval($order->getIncrementId());
						$getorder = Mage::getModel('sales/order')->load($order->getId());
						
						$xml_output .= "<dict>\n";
						$xml_output .=	"<key>orderid</key>\n";
						$xml_output .=	"<integer>".$order->getId()."</integer>\n";
						$xml_output .=	"<key>deliverytime</key>\n";
						$xml_output .=	"<string>".$deliverytime."</string>\n";
						$xml_output .=	"<key>discount</key>\n";
						$xml_output .=	"<real>".$order->getDiscountAmount()."</real>\n";
						$xml_output .=	"<key>subtotal</key>\n";
						$xml_output .=	"<real>".$order->getBaseSubtotal()."</real>\n";
						$xml_output .=	"<key>totalprice</key>\n";
						$xml_output .=	"<real>".$order->getGrandTotal()."</real>\n";
						$xml_output .=	"<key>status</key>\n";
						$xml_output .=	"<string>".$order->getStatus()."</string>\n";
						$xml_output .=	"<key>date</key>\n";
						$xml_output .=	"<date>".str_replace(" ","T",$order->getCreatedAt())."Z</date>\n";
						$items = $getorder->getAllItems();
						$itemcount=count($items);
						$xml_output .=	"<key>items</key>\n";
						$xml_output .= "<array>\n";
					
						foreach ($items as $itemId => $item)
						{	
							$xml_output .= "<dict>\n";
							$xml_output .=	"<key>id</key>\n";
							$xml_output .=	"<integer>".$item->getProductId()."</integer>\n";	
							$xml_output .=	"<key>name</key>\n";
							$xml_output .=	"<string>".$item->getName()."</string>\n";
							$xml_output .=	"<key>price</key>\n";
							$xml_output .=	"<real>".$item->getPrice()."</real>\n";						
							$xml_output .=	"<key>sku</key>\n";
							$xml_output .=	"<string>".$item->getSku()."</string>\n";		
							$xml_output .=	"<key>quantity</key>\n";
							$xml_output .=	"<real>".$item->getQtyOrdered()."</real>\n";	
							$xml_output .=	"</dict>";
						}
						
						$xml_output .= "</array>\n";
						$xml_output .=	"</dict>";						
					}						
				}	
				$xml_output .= "</array>\n";
				$xml_output .=	"</dict>";	
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
$xml_output .= "</array>\n";			
$xml_output .= "</plist>\n";
echo $xml_output;
							

	