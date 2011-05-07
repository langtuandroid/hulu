<?php
class Zendvn_View_Helper_SearchPartical extends Zend_View_Helper_Abstract{

	public function searchPartical()
	{
			$html= ' <table width="184" border="0" cellpadding="0" cellspacing="0" id="table">
  <tr>
    <td><img src="'.$this->view->imgUrl  .'/timtheophankhuc.gif" width="184" height="30" /></td>
  </tr>
  <tr>
    <td id="infotable">
    <table width="100%" border="0" cellspacing="1" cellpadding="4">
  <tr>
    <td id="leftmenu"><a href="'. $this->view->url(array("controller"=>"search","action"=>"searchsell","getId"=>1),null,true) .'">Laptop Siêu Cấp</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'. $this->view->url(array("controller"=>"search","action"=>"searchsell","getId"=>2),null,true).'">Laptop Cao Cấp</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'. $this->view->url(array("controller"=>"search","action"=>"searchsell","getId"=>3),null,true).'">Laptop Trung Cấp</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'. $this->view->url(array("controller"=>"search","action"=>"searchsell","getId"=>4),null,true).'">Laptop Phổ Thông</a></td>
  </tr>
</table>    </td>
  </tr>
</table>';
		return $html;
	}
}