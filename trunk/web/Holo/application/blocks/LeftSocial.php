<?php
class Block_LeftSocial extends Zend_View_Helper_Abstract{
		public function leftSocial(){
		
//		$view  = $this->view;		
		require_once (BLOCK_PATH . '/LeftSocial/default.php');
	}
	
}