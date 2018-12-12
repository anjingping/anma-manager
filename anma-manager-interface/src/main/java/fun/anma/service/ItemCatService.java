package fun.anma.service;

import java.util.List;

import fun.anma.common.pojo.EasyUITreeNode;

public interface ItemCatService {

	//根据父节点的ID来查询树形结构
	//因为是懒加载，所以最开始显示第一级目录，点击下一级目录的时候才显示下一级
	public List<EasyUITreeNode> getItemCatList(Long parentId);
}
