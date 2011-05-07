<?php
class Zendvn_Controller_Action extends Zend_Controller_Action
{
	public function init()
	{
		/*$template_path=TEMPLATE_PATH.'/admin/system';
		$this->loadTemplate($template_path);*/
	}
	protected function loadTemplate($template_path,$fileConfig = 'template.ini',$sectionConfig = 'template')
	{
		
		$this->view->headTitle()->getContainer()->exchangeArray(array());
		$this->view->headMeta()->getContainer()->exchangeArray(array());
		$this->view->headScript()->getContainer()->exchangeArray(array());
		$this->view->headLink()->getContainer()->exchangeArray(array());
		$config = new Zend_Config_Ini($template_path.'/'.$fileConfig,$sectionConfig);
		$baseUrl=$this->_request->getBaseUrl();
		$templateUrl= $baseUrl.$config->url;
		$cssUrl= $templateUrl.$config->dirCss;
		$jsUrl= $templateUrl.$config->dirJs;
		$imgUrl = $templateUrl.$config->dirImg;
		$flashUrl = $templateUrl.$config->dirFlash;
		$this->view->templateUrl=$templateUrl;
		$this->view->imgUrl = $imgUrl;
		$this->view->flashUrl= $flashUrl;
		$layout=$config->layout;
		$this->view->headTitle()->append($config->title);
		$this->view->headLink(array('rel' => 'shortcut icon','href' => ''.$imgUrl.'/messenger.ico'),'PREPEND');
		if(count($config->metaHttp)>0)
		{
			foreach($config->metaHttp as $value)
			{
				$value1= explode("|",$value);
			    $this->view->headMeta()->appendHttpEquiv($value1[0],$value1[1]);
				
				
			}
		}
		if(count($config->metaName)>0)
		{
			foreach($config->metaName as $value)
			{
				$value1= explode("|",$value);
			    $this->view->headMeta()->appendName($value1[0],$value1[1]);
				
				
			}
		}
		if(count($config->fileCss)>0)
		{
			foreach($config->fileCss as $value)
			{
				
				  $this->view->headLink()->appendStylesheet($cssUrl.$value);
		
			}
		}
		if(count($config->fileJs)>0)
		{
			foreach($config->fileJs as $value)
			{
				
				  $this->view->headScript()->appendFile($jsUrl.$value);
		
			}
		}
		
		$option = array("layoutPath"=>$template_path,
						"layout"=> $layout,
						"viewSuffix"=>'phtml');
		Zend_Layout::startMvc($option);
		
	}
}