package fun.anma.service;

import fun.anma.common.pojo.AnmaResult;
import fun.anma.common.pojo.EasyUIDataGridResult;
import fun.anma.pojo.TbItem;
import fun.anma.pojo.TbItemDesc;

/**
 * @author 安京平
 *处理商品接口
 */
public interface ItemService {
	//根据id查商品
	public TbItem getItemById(long itemId);
	//获取商品列表
	public EasyUIDataGridResult getItemList(int page,int rows);
	//添加商品
	public AnmaResult createItem(TbItem tbItem,String desc);
	//根据id查询商品描述
	public TbItemDesc geTbItemDescById(long itemId);
}
