<?php
header("Content-type: text/xml"); 
require_once('../app/Mage.php');
Mage::App('default');

	$_categories = Mage::getModel('catalog/category')
                    ->getCollection()
                    ->addAttributeToSelect('*')
                    ->addIsActiveFilter()
                    ->addAttributeToFilter('level',2) 
                    ->addOrderField('name');
	
	$xml_output = "<?xml version=\"1.0\"?>\n";
	$xml_output .= "<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\"
\"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">";
	$xml_output .= "<plist version=\"1.0\">\n";
	$xml_output .= "<array>\n";
	
$colors = array("#00AEEF","#C1D82F","#FFBE00","#652D89","#D60C8C","#FB4100");
$i=0;
if (count($_categories) !=0) {  
    
        foreach($_categories as $_category) 
		{
			
				$_cat = Mage::getModel('catalog/category')->load($_category->getId());
                $_subcategories = $_cat->getChildrenCategories();
                 
				 if (count($_subcategories) > 0): 
                 
					foreach($_subcategories as $_subcategory) 
					{
						$_subcategory = Mage::getModel('catalog/category')->load($_subcategory->getId()); 
                           
									$xml_output .= "<dict>\n";
									$xml_output .=	"<key>id</key>\n";
									$xml_output .=	"<integer>".$_subcategory->getId()."</integer>\n";
									$xml_output .=	"<key>name</key>\n";
									$xml_output .=	"<string>".$_subcategory->getName()."</string>\n";
									$xml_output .=	"<key>color</key>";
									$xml_output .=	"<string>".$colors[$i]."</string>\n";
									$xml_output .=	"<key>image</key>";
									$xml_output .=	"<string>".$_subcategory->getImageUrl()."</string>\n";
									$xml_output .=	"</dict>";
						$i++;
						if($i==5)
						{
							$i=0;
						}
					} 
                    
                 endif; 
       
        }
		
     }
	 
$xml_output .= "</array>\n";
$xml_output .= "</plist>\n";
echo $xml_output;