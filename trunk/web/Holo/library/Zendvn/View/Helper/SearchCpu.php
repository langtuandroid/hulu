<?php
class Zendvn_View_Helper_SearchCpu extends Zend_View_Helper_Abstract{

	public function searchCpu()
	{
			$html= '<table width="184" border="0" cellpadding="0" cellspacing="0" id="table">
  <tr>
    <td><img src="'.$this->view->imgUrl.'/timtheobovisuly.gif" width="184" height="33" /></td>
  </tr>
  <tr>
    <td id="infotable">
    <table width="100%" border="0" cellspacing="1" cellpadding="4">
  <tr>
    <td id="leftmenu"><a href="'. $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"AMD Althon"),null,true).'">AMD Althon</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"AMD Sempron"),null,true).'">AMD Sempron</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"AMD Turion"),null,true).'">AMD Turion</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'. $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Centrino Core Duo"),null,true).'">Centrino Core Duo</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Intel Atom"),null,true).'">Intel Atom</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Intel Celeron"),null,true).'">Intel Celeron</a></td>
  </tr>
  <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Intel Core 2 Duo"),null,true).'">Intel Core 2 Duo</a></td>
  </tr>
   <tr>
    <td id="leftmenu"><a href="'. $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Intel Core 2 Solo"),null,true).'">Intel Core 2 Solo</a></td>
  </tr>
   <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Intel Core i3"),null,true).'">Intel Core i3</a></td>
  </tr>
   <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Intel Core i5"),null,true).'">Intel Core i5</a></td>
  </tr>
   <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"Intel Core i7"),null,true).'">Intel Core i7</a></td>
  </tr>
   <tr>
    <td id="leftmenu"><a href="'.  $this->view->url(array("controller"=>"search","action"=>"result","getInfo"=>"VIA C7-M"),null,true).'">VIA C7-M</a></td>
  </tr>
</table>    </td>
  </tr>
</table>';
		return $html;
	}
}