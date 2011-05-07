<?php
require_once (DIR_SCRIPTS . '/fckeditor/fckeditor.php');
class Zendvn_View_Helper_CmsEditor extends Zend_View_Helper_Abstract{
	
	public function cmsEditor($name = 'content',$value = '',$width = '100%',$height = 500){
		
		$oFCKeditor = new FCKeditor($name);
		$oFCKeditor->BasePath = URL_SCRIPT . '/fckeditor/' ;
		$oFCKeditor->Width = $width;
		$oFCKeditor->Height = $height;
		$oFCKeditor->Value = $value ;
		/*echo '<pre>';
		print_r($oFCKeditor);
		echo '</pre>';*/
		return $oFCKeditor->Create() ;
	}
}