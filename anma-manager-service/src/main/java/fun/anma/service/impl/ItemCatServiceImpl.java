package fun.anma.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fun.anma.common.pojo.EasyUITreeNode;
import fun.anma.mapper.TbItemCatMapper;
import fun.anma.pojo.TbItemCat;
import fun.anma.pojo.TbItemCatExample;
import fun.anma.pojo.TbItemCatExample.Criteria;
import fun.anma.service.ItemCatService;
 

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		// TODO Auto-generated method stub
		//根据父节点id查询子节点列表
		TbItemCatExample catExample = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = catExample.createCriteria();
		//设置parentId
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> tbItemCatList = itemCatMapper.selectByExample(catExample);
		
		//将tbItemCat 的集合转换为EasyUITreeNode的集合传回去显示
		ArrayList<EasyUITreeNode> easyUITreeNodeList = new ArrayList<EasyUITreeNode>();
		
		for (TbItemCat tbItemCat : tbItemCatList) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			easyUITreeNodeList.add(easyUITreeNode);
		}
		
		return easyUITreeNodeList;
	}

}
