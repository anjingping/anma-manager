package fun.anma.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;import org.springframework.validation.DirectFieldBindingResult;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import fun.anma.common.pojo.AnmaResult;
import fun.anma.common.pojo.EasyUIDataGridResult;
import fun.anma.common.utils.IDutils;
import fun.anma.mapper.TbItemDescMapper;
import fun.anma.mapper.TbItemMapper;
import fun.anma.pojo.TbItem;
import fun.anma.pojo.TbItemDesc;
import fun.anma.pojo.TbItemExample;
import fun.anma.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name = "itemAddTopic")
	private Destination destination;
	
	@Override
	public TbItem getItemById(long itemId) {
		// TODO Auto-generated method stub
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		
		List<TbItem> list = itemMapper.selectByExample(example);
		//获取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		
		
		return result;
	}

	@Override
	public AnmaResult createItem(TbItem tbItem, String desc) {
		//生成商品ID
		long itemId = IDutils.genItemId();
		//补全Item的属性
		tbItem.setId(itemId);
		tbItem.setStatus((byte)1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		itemMapper.insert(tbItem);
		//添加商品描述
		insertItemDesc(itemId, desc);
		
		//通过activeMq发送topic消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		
		return AnmaResult.ok();
	}
	
	public void insertItemDesc(Long itemId,String desc){
		//创建商品描述表对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		//对应商品id
		itemDesc.setItemId(itemId);
		//商品描述
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向表中插入数据
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public TbItemDesc geTbItemDescById(long itemId) {
		
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		return itemDesc;
	}

}
