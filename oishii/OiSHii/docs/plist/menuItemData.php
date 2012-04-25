<?php
header("Content-type: text/xml"); 
require_once('../app/Mage.php');
Mage::App('default');
$args = $_REQUEST['catID'];
$_categories = Mage::getModel('catalog/category')
                    ->getCollection()
                    ->addAttributeToSelect('*')
                    ->addIsActiveFilter()
                    ->addLevelFilter(2)
                    ->addOrderField('name');
	
	$xml_output = "<?xml version=\"1.0\"?>\n";
	$xml_output .= "<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"
\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">";
	$xml_output .= "<plist version=\"1.0\">\n";
	$xml_output .= "<array>\n";
	
$colors = array("#00AEEF","#C1D82F","#FFBE00","#652D89","#D60C8C","#FB4100");
$i=0;

				$_category = Mage::getModel('catalog/category')->load($args);
                $_subcategories = $_category->getChildrenCategories();
                 
				 if (count($_subcategories) > 0): 
                 
					foreach($_subcategories as $_subcategory) 
					{
						 $_subcategory = Mage::getModel('catalog/category')->load($_subcategory->getId()); 
                           
									$xml_output .= "<dict>\n";
									$xml_output .=	"<key>id</key>\n";
									$xml_output .=	"<integer>".$_subcategory->getId()."</integer>\n";
									$xml_output .=	"<key>name</key>\n";
									$xml_output .=	"<string>".$_subcategory->getName()."</string>\n";
									$xml_output .=	"<key>shortdescription</key>";
									$xml_output .=	"<string>".$_subcategory->getDescription()."</string>\n";
									$xml_output .=	"<key>items</key>\n";
									$xml_output .=	"<array>\n";
									
									/************* Product Listing *******************/
									$category_pro = new Mage_Catalog_Model_Category();
									$category_pro->load($_subcategory->getId());
									$collection = $category_pro->getProductCollection(); 
									$last = sizeof($collection);
									$cnt = 1;
									foreach ($collection as $product) 
									{	
										 
											$product= Mage::getModel('catalog/product')->load($product->getId());	//For Product Quanity
											$productStockItem = $product->getStockItem();
									/************* Product Listing *******************/
									$xml_output .=	"<dict>
									  <key>id</key>
									  <integer>".$product->getId()."</integer>
									  <key>name</key>
									  <string>".$product->getName()."</string>
									  <key>image</key>
									  <string>".$product->getImageUrl()."</string>
									  <key>shortdescription</key>
									  <string>".$product->getDescription()."</string>
									  <key>itemsremaining</key>
									  <integer>".intval($productStockItem->getQty())."</integer>
									  <key>price</key>
									  <real>".$product->getFinalPrice()."</real>
									</dict>";
									}
									$xml_output .=	"</array>\n";
									$xml_output .=	"</dict>\n";
                                
								
						
						$i++;
						if($i==5)
						{
							$i=0;
						}
					} 
                    
                 endif; 
       
        
$xml_output .= "</array>\n";
$xml_output .= "</plist>\n";
echo $xml_output;