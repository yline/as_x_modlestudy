package com.dm.gongchang.factory.reflact;

import com.dm.gongchang.product.Product;

public abstract class Factory {
	/**
	 * 抽象工厂方法
	 * 具体生产什么由子类去实现
	 * 
	 * @return	具体产品对象
	 */
	public abstract <T extends Product> T createProduct(Class<T> clz);
}
