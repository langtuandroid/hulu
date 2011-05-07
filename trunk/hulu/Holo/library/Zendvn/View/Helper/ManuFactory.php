<?php
class Zendvn_View_Helper_ManuFactory extends Zend_View_Helper_Abstract{
	protected  $menu ; 
	public function __construct()
	{
		$this->menu = new Zendvn_Menu();
	}

	public function manuFactory()
	{
			$html= 	'<table width="184" border="0" cellpadding="0" cellspacing="0" id="table">
  <tr>
    <td><img src="'.$this->view->imgUrl .'/cachanglaptop.gif" width="184" height="34" /></td>
  </tr>
  <tr>
    <td id="infotable">
    <table width="100%" border="0" cellspacing="1" cellpadding="4">';
    foreach($this->menu->menu() as $value)
    {
    	
 $html .=' <tr>
    <td id="leftmenu"><a href="'. 
 $this->view->url(array("controller"=>"product","action"=>"index","getCat"=>$value['idLoai'])).'">'. $value['TenLoai'].
 '</a></td>
  </tr>';
 
 
 
    }
$html.='
</table>    </td>
  </tr>
</table>';
		return $html;
	}
}