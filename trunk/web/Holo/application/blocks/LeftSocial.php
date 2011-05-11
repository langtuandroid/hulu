<?php
class Block_BlkCategoryMenu extends Zend_View_Helper_Abstract{
		public function LeftSocial(){
		
		$view  = $this->view;		
		require_once (BLOCK_PATH . '/LeftSocial/default.php');
	}
	
}