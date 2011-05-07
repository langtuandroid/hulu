<?php
class Zendvn_View_Helper_OrderProduct extends Zend_View_Helper_Abstract{
	
	public function getorderProduct(){
		$count=0;
		$session= new Zend_Session_Namespace();
		if(count($session->arr))
			$count= count($session->arr)-1;
		else
			$count= 0;
		return $count;
		
	
	}
	public function orderProduct()
	{
		
		$html='<div id="divoder"><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="'.$this->view->imgUrl.'/xehang.gif" width="43" height="42" /></td>
    <td>Giỏ Hàng Của Bạn:'.
$this->getorderProduct().'<p>
<a href="'.$this->view->url(array("controller"=>"order","action"=>"index"),null,true).'" >Xem Chi Tiet</a></p></td>
  </tr>
</table>

</div>';
 return $html;
	}
}