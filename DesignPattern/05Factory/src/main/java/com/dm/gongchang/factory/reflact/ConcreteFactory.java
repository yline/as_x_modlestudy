package com.dm.gongchang.factory.reflact;

import com.dm.gongchang.product.Product;

/**
 * 这样依照反射写的好处是,能够依据传参就确定了生产哪类产品
 * @author f21
 * @date 2016-2-25
 */
public class ConcreteFactory extends Factory{

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Product> T createProduct(Class<T> clz) {
		Product product = null;
		try {
			product = (Product) Class.forName(clz.getName()).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return (T) product;
	}
	
}
