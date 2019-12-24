package com.nousin.springcloud.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;

/**
 * 对象转换工具类
 * @author Nousin
 * @since 2019/12/16
 */
public class DozerUtil {

	static Mapper mapper = new DozerBeanMapper();


	/**
	 * 将对象转成目标对象并复制属性值
	 * @param obj
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public static <T> T map(Object obj, Class<T> tClass) {
		return mapper.map(obj, tClass);
	}

	/**
	 * 将source的所有属性拷贝至target,source里没有的字段,target里不覆盖
	 * @param source
	 * @param target
	 * @return
	 */
	public static <U> void map(final Object source, final U target) {
		DozerBeanMapper mapper = new DozerBeanMapper();
		mapper.addMapping(new BeanMappingBuilder() {
			@Override
			protected void configure() {
				mapping(source.getClass(), target.getClass(), TypeMappingOptions.mapNull(false));
			}

		});
		mapper.map(source, target);
	}

	/**
	 * 转换List中对象的类型.
	 * @param source
	 * @param destType
	 * @return
	 */
	public static <T, U> List<U> mapList(final List<T> source, final Class<U> destType) {
		final List<U> dest = new ArrayList<U>();
		for (T element : source) {
			dest.add(mapper.map(element, destType));
		}
		return dest;
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 * @param source List
	 * @param destinationClass
	 * @param <T>
	 * @return List<T>
	 */
	public static <T> List<T> mapList(Collection<?> source, Class<T> destinationClass) {
		List<T> destinationList = new ArrayList<T>();
		for (Object sourceObject : source) {
			T destinationObject = mapper.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}


	/**
	 * 将Collection<E>
	 * @param source
	 * @param destType
	 * @param <T>
	 * @param <U>
	 * @return
	 */
	public static <T, U> Collection<U> mapCollection(final Collection<T> source, final Class<U> destType) {
		final Collection<U> dest = new ArrayList<>();
		for (T element : source) {
			dest.add(mapper.map(element, destType));
		}
		return dest;
	}

	/**
	 * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖tagetBean重点的值
	 *
	 * @param sourceBean 被提取的对象bean
	 * @param targetBean 用于合并的对象bean
	 * @return targetBean 合并后的对象
	 * @return: Object
	 */
	@SuppressWarnings("unused")
	public static Object map2(Object sourceBean, Object targetBean) {
		Class sourceBeanClass = sourceBean.getClass();
		Class targetBeanClass = targetBean.getClass();

		Field[] sourceFields = sourceBeanClass.getDeclaredFields();
		Field[] targetFields = sourceBeanClass.getDeclaredFields();
		for (int i = 0; i < sourceFields.length; i++) {
			Field sourceField = sourceFields[i];
			Field targetField = targetFields[i];
			sourceField.setAccessible(true);
			targetField.setAccessible(true);
			try {
				if (!(sourceField.get(sourceBean) == null)) {
					targetField.set(targetBean, sourceField.get(sourceBean));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return targetBean;
	}

}
