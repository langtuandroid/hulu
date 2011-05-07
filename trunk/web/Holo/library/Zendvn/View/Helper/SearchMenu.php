l<?php
class Zendvn_View_Helper_SearchMenu extends Zend_View_Helper_Abstract{
	protected  $menu ; 
	public function __construct()
	{
		$this->menu = new Zendvn_Menu();
	}

	public function searchMenu()
	{		
			$html= ' <form name="search" id="search" method="POST" action="'.$this->view->url(array("controller"=>"search","action"=>"search"),null,true).'">
			  <table width="184" border="0" cellpadding="0" cellspacing="0" id="table">
			  <tr>
			    <td><img src="'.$this->view->imgUrl  .'/timkiemlaptop.gif" width="184" height="32" /></td>
			  </tr>
			  <tr>
			    <td id="infotable"><table width="100%" cellspacing="8" cellpadding="0" border="0">
	                <tbody><tr>
	                    <td valign="top">
	                      <input type="text" onclick="this.value=\'\'" id="product"  name="txtKey"></td>
	                </tr>
	            
	                <tr>
	                  <td>
	                  
	                  
	                  <select style="width: 98%;"   id="category" name="category">
			<option value=0>- Chọn  Hãng -</option>';	   
		   
		    foreach($this->menu->menu() as $value)
		    {
		    	$html.= '<option value='. $value['idLoai'].'>'.$value['TenLoai'].'</option>';
		    }
	
			$html .= '</select>
			</td>
			</tr>
	                <tr>
	                  <td>
	                  
	                  	<select style="width: 98%;" class="timkiemlist" id="price1" name="price1">
							<option value=0>- GiÃ¡ tá»« -</option>
							<option value=5000>5 triá»‡u</option>
							<option value=10000>10 triá»‡u</option>
							<option value=15000>15 triá»‡u</option>
							<option value=20000>20 triá»‡u</option>
							<option value=25000>25 triá»‡u</option>
							<option value=25000>&gt;25 triá»‡u</option>
						</select>                  </td>
	                </tr>
	                <tr>
	                  <td>
	                  
	                  <select style="width: 98%;" class="timkiemlist" id="price2" name="price2">
							<option value=0>- Ä�áº¿n -</option>
							<option value=10000>10 triá»‡u</option>
							<option value=15000>15 triá»‡u</option>
							<option value=20000>20 triá»‡u</option>
							<option value=25000>25 triá»‡u</option>
						</select>                    </td>
						                </tr>
	                <tr>
	                  <td align="center">
	                  
	                  <img onClick="search.submit();" height="22" width="80" border="0" src="'. $this->view->imgUrl.'/timkiem_but.gif" />
	                                       </td>
	                </tr>
	          </tbody></table></td>
	  </tr>
	</table>
	</form>
	  </td>
    </tr>
    </table>';
		return $html;
	}
}