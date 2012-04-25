<?php
header("Content-type: text/xml"); 
require_once('../app/Mage.php');
Mage::App('default');

	// Retrieve the layout object
    $layout = Mage::getSingleton('core/layout');
 
    // Generate a CMS block object
    $block = $layout->createBlock('cms/block');
 
    // Set the block ID of the static block
    $block->setBlockId('offers-plist');
 
    // Write the static block content to screen
    //echo html_entity_decode($block->toHtml());
	
	$html =  str_replace("&nbsp;","",$block->toHtml());
	
	$html =  str_replace("<p>","",$html);
	
	$html =  str_replace("</p>","",$html);
	
	$html =  str_replace("<br />","",$html);
	
	$html =  str_replace("&lt;","<",$html);
	
	$html =  str_replace("&gt;",">",$html);
	
	$html =  str_replace("&pound;","&#163;",$html);
	
	echo $html;