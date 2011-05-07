<?php
class Zendvn_Menu{
	
	public function menu(){
		
		$category=new Default_Model_Category();
		return $category->getCategory();
	
	}


}