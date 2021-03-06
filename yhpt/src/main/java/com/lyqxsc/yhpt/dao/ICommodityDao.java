package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.CommodityBak;


/**
 * 总部出售商品和租赁商品数据库接口
 */
@Mapper
@Component
public interface ICommodityDao {
	
	@Select("select sum(sales) from commoditylist")
	Integer getTotalSales();
	
	@Select("select max(id) from commoditylist")
	Long getMaxID();
	
	@Select("select max(ordernumTotal) from commoditylist")
	Integer getMaxOrdernum();
	
	@Select("select * from commoditylist where id=#{id}")
	Commodity selectCommodityByID(@Param("id") long id);
	
	//查询所有商品
	@Select("select * from commoditylist")
	List<Commodity> selectAll();
	
	@Select("select * from commoditylist where type=2 or type=3")
	List<Commodity> selectCommodity();
	
	@Select("select * from commoditylist where type=1 or type=3")
	List<Commodity> selectRentCommodity();
	

	@Insert({"insert into commoditylist(id,name,picurl,price,price1,price2,price3,price4,price5,price6,rentPrice,rentPrice1,rentPrice2,rentPrice3,rentPrice4,rentPrice5,rentPrice6,type,inventory,sales,ordernumDay,ordernumMouth,ordernumTotal,salesVolumeDay,salesVolumeMouth,"
			+ "salesPriceDay,salesPriceMouth,salesPriceTotal,rentOrdernumDay,rentOrdernumMouth,rentOrdernumTotal,rentVolumeDay,rentVolumeMouth,rentVolumeTotal,rentPriceDay,rentPriceMouth,rentPriceTotal,deposit,note,distributor,kind,classId,classStr,online,onlineTime,addTime) "
			+ "values(#{id},#{name},#{picurl},#{price},#{price1},#{price2},#{price3},#{price4},#{price5},#{price6},#{rentPrice},#{rentPrice1},#{rentPrice2},#{rentPrice3},#{rentPrice4},#{rentPrice5},#{rentPrice6},#{type},#{inventory},#{sales},#{ordernumDay},#{ordernumMouth},"
			+ "#{ordernumTotal},#{salesVolumeDay},#{salesVolumeMouth},#{salesPriceDay},#{salesPriceMouth},#{salesPriceTotal},#{rentOrdernumDay},#{rentOrdernumMouth},#{rentOrdernumTotal},#{rentVolumeDay},#{rentVolumeMouth},#{rentVolumeTotal},#{rentPriceDay},#{rentPriceMouth},#{rentPriceTotal},#{deposit},#{note},#{distributor},#{kind},#{classId},#{classStr},#{online},#{onlineTime},#{addTime})"})
	int addCommodity(Commodity commodity);
	
	@Update("update commoditylist set name=#{name},picurl=#{picurl},price=#{price},price1=#{price1},price2=#{price2},price3=#{price3},price4=#{price4},price5=#{price5},price6=#{price6},rentPrice=#{rentPrice},rentPrice1=#{rentPrice1},rentPrice2=#{rentPrice2},rentPrice3=#{rentPrice3},rentPrice4=#{rentPrice4},rentPrice5=#{rentPrice5},rentPrice6=#{rentPrice6},type=#{type},inventory=#{inventory},deposit=#{deposit},note=#{note},kind=#{kind},classId=#{classId},classStr=#{classStr},online=#{online}"
			+ " where id=#{id}")
	int updateCommodity(Commodity commodity);
	/*
	 * 分销商三连
	 */
	@Select("select * from commoditylist where distributor=#{distributor}")
	List<Commodity> selectAllByDistributor(@Param("distributor") long distributor);
	
	//获取分销商的商品 1租赁 2出售 3租赁出售
	@Select("select * from commoditylist where (type=2 or type=3) and (distributor=#{distributor} or distributor=0)")
	List<Commodity> selectCommodityForUser(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where (type=2 or type=3) and (distributor=#{distributor} or distributor=0)")
	List<CommodityBak> selectCommodityBakForUser(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where (type=1 or type=3) and (distributor=#{distributor} or distributor=0)")
	List<Commodity> selectRentCommodityForUser(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where (type=1 or type=3) and (distributor=#{distributor} or distributor=0)")
	List<CommodityBak> selectRentCommodityBakForUser(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where distributor=#{distributor} or distributor=0")
	List<CommodityBak> selectAllCommodityForUser(@Param("distributor") long distributor);
	
	//根据物品id查询分销商的商品
	@Select("select * from commoditylist where id=#{id} and (distributor=#{distributor} or distributor=0)")
	Commodity selectCommodityByIDForUser(@Param("id")long id, @Param("distributor") long distributor);
	
	//根据物品id查询分销商的商品
	@Select("select * from commoditylist where id=#{id} and (distributor=#{distributor} or distributor=0)")
	CommodityBak selectCommodityBakByIDForUser(@Param("id")long id, @Param("distributor") long distributor);
	
	//根据分销商和物品ID查询物品
	@Select("select * from commoditylist where distributor=#{distributor} and id=#{id}")
	Commodity selectNewCommodityByDistributor(@Param("distributor") long distributor,@Param("id") long id);
	
	//根据分销商和物品ID查询物品
	@Select("select * from commoditylist where distributor=#{distributor} and id=#{id}")
	CommodityBak selectNewCommodityBakByDistributor(@Param("distributor") long distributor,@Param("id") long id);
	
	//根据分销商和物品订单数查询物品
	@Select("select * from commoditylist where distributor=#{distributor} and ordernumTotal=#{ordernumTotal}")
	Commodity selectHotCommodityByDistributor(@Param("distributor") long distributor,@Param("ordernumTotal") int ordernum);
	
	//根据分销商和物品订单数查询物品
	@Select("select * from commoditylist where distributor=#{distributor} and ordernumTotal=#{ordernumTotal}")
	List<CommodityBak> selectHotCommodityBakByDistributor(@Param("distributor") long distributor,@Param("ordernumTotal") int ordernum);

	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId}")
	List<Commodity> selectCommodityByClass(@Param("distributor") long distributor, @Param("classId") int classId);
	
	//根据物品分类及价格区间查询物品
	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId} and price BETWEEN #{min} and #{max}")
	List<CommodityBak> selectCommodityBakByClass(@Param("distributor") long distributor, @Param("classId") int classId, @Param("min") float min, @Param("max") float max);
	
	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId} and price1 BETWEEN #{min} and #{max}")
	List<CommodityBak> selectCommodityBakByClass1(@Param("distributor") long distributor, @Param("classId") int classId, @Param("min") float min, @Param("max") float max);
	
	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId} and price2 BETWEEN #{min} and #{max}")
	List<CommodityBak> selectCommodityBakByClass2(@Param("distributor") long distributor, @Param("classId") int classId, @Param("min") float min, @Param("max") float max);
	
	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId} and price3 BETWEEN #{min} and #{max}")
	List<CommodityBak> selectCommodityBakByClass3(@Param("distributor") long distributor, @Param("classId") int classId, @Param("min") float min, @Param("max") float max);
	
	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId} and price4 BETWEEN #{min} and #{max}")
	List<CommodityBak> selectCommodityBakByClass4(@Param("distributor") long distributor, @Param("classId") int classId, @Param("min") float min, @Param("max") float max);
	
	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId} and price5 BETWEEN #{min} and #{max}")
	List<CommodityBak> selectCommodityBakByClass5(@Param("distributor") long distributor, @Param("classId") int classId, @Param("min") float min, @Param("max") float max);
	
	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId} and price6 BETWEEN #{min} and #{max}")
	List<CommodityBak> selectCommodityBakByClass6(@Param("distributor") long distributor, @Param("classId") int classId, @Param("min") float min, @Param("max") float max);
	
	@Select("select * from commoditylist where distributor=#{distributor} and name=#{name}")
	List<Commodity> selectCommodityByName(@Param("distributor") long distributor, @Param("name") String name);

	//用户按名称查询物品
	@Select("select * from commoditylist where distributor=#{distributor} and name=#{name}")
	List<CommodityBak> selectCommodityBakByName(@Param("distributor") long distributor, @Param("name") String name);

	
	//告警商品,管理员
	@Select("select * from commoditylist where inventory<=#{inventory} and distributor=#{id}")
	List<Commodity> inventoryWarning(@Param("inventory") int num,@Param("id") long id);
	//不告警商品,管理员
	@Select("select * from commoditylist where inventory>#{inventory} and distributor=#{id}")
	List<Commodity> inventoryNoWarning(@Param("inventory") int num,@Param("id") long id);
	
	@Delete("delete from commoditylist where id=#{id}")
	int removeCommodity(@Param("id") long id);
	
	@Delete("delete from commoditylist where id=#{id} and distributor=#{distributor}")
	int removeCommodityByDistributor(@Param("id") long id, @Param("distributor")long distributor);
	
	
//	@Update("update commoditylist set name=#{name},picurl=#{picurl},price=#{price},type=#{type},inventory=#{inventory},ordernum=#{ordernum},deposit=#{deposit},note=#{note}"
//			+ "where id=#{id}")
//	int updateCommodity(Commodity commodity);
	
	//物品上架/下架
	@Update("update commoditylist set online=#{online} where id=#{id}")
	int updateOnline(@Param("id") long id,@Param("online") int option);
		
	//更新商品订单数
	@Update("update commoditylist set "
			+ "ordernumDay=ordernumDay+1,"
			+ "ordernumMouth=ordernumMouth+1,"
			+ "ordernumTotal=ordernumTotal+1 "
			+ "where id=#{id}")
	int updatePayOrderNum(@Param("id") long id);
	
	//更新商品销量
	@Update("update commoditylist set "
			+ "salesVolumeDay=salesVolumeDay+#{salesVolume},"
			+ "salesVolumeMouth=salesVolumeMouth+#{salesVolume},"
			+ "sales=sales+#{salesVolume},"
			+ "salesPriceDay=salesPriceDay+#{salesPrice},"
			+ "salesPriceMouth=salesPriceMouth+#{salesPrice},"
			+ "salesPriceTotal=salesPriceTotal+#{salesPrice} "
			+ "where id=#{id}")
	int updateSalesVolume(@Param("salesVolume") int salesVolume,@Param("salesPrice") float salesPrice, @Param("id") long id);
	
	//日销量,订单数清0
	@Update("update commoditylist set salesVolumeDay=0,salesPriceDay=0,ordernumDay=0 where id=#{id}")
	int clearSalesVolumeDay(@Param("id") long id);
	
	//月销量，订单数清0
	@Update("update commoditylist set salesVolumeMouth=0,salesPriceMouth=0,ordernumMouth where id=#{id}")
	int clearSalesVolumeMouth(@Param("id") long id);
	
	
	//更新商品租赁订单数
	@Update("update commoditylist set "
			+ "rentOrdernumDay=rentOrdernumDay+1,"
			+ "rentOrdernumMouth=rentOrdernumMouth+1,"
			+ "rentOrdernumTotal=rentOrdernumTotal+1 "
			+ "where id=#{id}")
	int updateRentOrderNum(@Param("id") long id);
	
	//更新商品销量
	@Update("update commoditylist set "
			+ "rentVolumeDay=rentVolumeDay+#{rentVolume},"
			+ "rentVolumeMouth=rentVolumeMouth+#{rentVolume},"
			+ "rentVolumeTotal=rentVolumeTotal+#{rentVolume},"
			+ "rentPriceDay=rentPriceDay+#{rentPrice},"
			+ "rentPriceMouth=rentPriceMouth+#{rentPrice},"
			+ "rentPriceTotal=rentPriceTotal+#{rentPrice} "
			+ "where id=#{id}")
	int updateRentVolume(@Param("rentVolume") int rentVolume,@Param("rentPrice") float rentPrice, @Param("id") long id);
	
	//日销量,租赁订单数清0
	@Update("update commoditylist set rentVolumeDay=0,rentPriceDay=0,rentOrdernumDay=0 where id=#{id}")
	int clearRentVolumeDay(@Param("id") long id);
	
	//月销量，租赁订单数清0
	@Update("update commoditylist set rentVolumeMouth=0,rentPriceMouth=0,rentOrdernumMouth where id=#{id}")
	int clearRentVolumeMouth(@Param("id") long id);


	
	
	//设置商品数量
	@Update("update commoditylist set inventory=inventory+#{count} where id=#{id}")
	int setCommodityCount(@Param("id")long id, @Param("count") int count);
	
	//获取商品销量
	@Select("select "
			+ "id,name,picurl,price,rentPrice,type,salesVolumeDay,salesVolumeMouth,sales,salesPriceDay,"
			+ "salesPriceMouth,salesPriceTotal,rentVolumeDay,rentVolumeMouth,rentVolumeTotal,rentPriceDay,"
			+ "rentPriceMouth,rentPriceTotal from commoditylist")
	List<SalesTabel> getSalesTabel();
	
	
	
}
