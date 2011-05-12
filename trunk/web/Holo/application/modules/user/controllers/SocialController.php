<?php
class SocialController extends Zendvn_Controller_Action{
	public function init()
	{	
		$template_path=TEMPLATE_PATH."/user/system";
		$this->loadTemplate($template_path);	
//		 $option = array('layoutPath'=>$template_path,'layout'=> 'header','viewSuffix'=>'php');
//		Zend_Layout::startMvc($option);
	}
	public function indexAction() {
	
	}
}