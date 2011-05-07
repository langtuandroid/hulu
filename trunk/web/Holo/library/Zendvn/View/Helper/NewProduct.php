<?php
class Zendvn_View_Helper_NewProduct extends Zend_View_Helper_Abstract{
	public function getNewProduct() {
		$procduct= new Default_Model_Product();
		return $procduct->getProductNew();
	}
	public function newProduct()
	{
	
		
		$html='<table width="184" border="0" cellpadding="0" cellspacing="0" id="table">
  <tr>
    <td><img src="'.$this->view->imgUrl.'/laptopmoive.gif" width="184" height="33" /></td>
  </tr>
  <tr>
    <td id="infotable">';
    
	foreach ($this->getNewProduct() as  $value) {

    $html.='<table width="100%" border="0" cellspacing="0" cellpadding="2"  >
    
      <tr>
        <td colspan="2" align="center">
        <a href="'.$this->view->url(array("controller"=>"product","action"=>"descript","getProduct"=>$value['idSP']),null,true).'">'.
    $value['TenSP'].'</a></td>
        </tr>
      <tr>
        <td><img src="/zendshopping/public/upload/'.$value['Image'].'" width="71" height="52" /></td>
        <td class="price">'.number_format($value['Gia'],3).' VND</td>
      </tr>
      <tr>
        <td colspan="2" align="center" class="fontvat">Giá Đã Bao Gồm 10% Vat</td>
        </tr>
 
    </table>';
         
    }
   
   
    
   $html.=' </td>
  </tr>
</table>';
   return $html;
	}
	
}