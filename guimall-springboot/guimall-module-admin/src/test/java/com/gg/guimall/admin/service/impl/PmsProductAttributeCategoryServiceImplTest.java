package com.gg.guimall.admin.service.impl;

import com.gg.guimall.admin.model.vo.pms.PmsProductAttributeCategoryCreateReqVO;
import com.gg.guimall.admin.model.vo.pms.PmsProductAttributeCategoryUpdateReqVO;
import com.gg.guimall.common.domain.dos.PmsProductAttributeCategoryDO;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性分类 Service 单元测试
 **/
@ExtendWith(MockitoExtension.class)
public class PmsProductAttributeCategoryServiceImplTest {

    @Mock
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Mock
    private PmsProductAttributeMapper attributeMapper;

    @InjectMocks
    private PmsProductAttributeCategoryServiceImpl attributeCategoryService;

    @Test
    void testCreateAttributeCategory_Success() {
        PmsProductAttributeCategoryCreateReqVO reqVO = new PmsProductAttributeCategoryCreateReqVO();
        reqVO.setName("测试属性分类");

        when(attributeCategoryMapper.selectOne(any())).thenReturn(null);

        Response response = attributeCategoryService.createAttributeCategory(reqVO);

        Assertions.assertTrue(response.isSuccess());
        verify(attributeCategoryMapper, times(1)).insert(argThat(category ->
                category.getName().equals("测试属性分类") &&
                category.getAttributeCount() == 0 &&
                category.getParamCount() == 0
        ));
    }

    @Test
    void testCreateAttributeCategory_NameExists() {
        PmsProductAttributeCategoryCreateReqVO reqVO = new PmsProductAttributeCategoryCreateReqVO();
        reqVO.setName("已存在的分类");

        PmsProductAttributeCategoryDO existCategory = new PmsProductAttributeCategoryDO();
        existCategory.setId(1L);
        existCategory.setName("已存在的分类");

        when(attributeCategoryMapper.selectOne(any())).thenReturn(existCategory);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            attributeCategoryService.createAttributeCategory(reqVO);
        });

        Assertions.assertEquals(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NAME_EXISTS.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void testUpdateAttributeCategory_Success() {
        PmsProductAttributeCategoryUpdateReqVO reqVO = new PmsProductAttributeCategoryUpdateReqVO();
        reqVO.setId(1L);
        reqVO.setName("更新后的分类名称");

        PmsProductAttributeCategoryDO existCategory = new PmsProductAttributeCategoryDO();
        existCategory.setId(1L);
        existCategory.setName("原分类名称");

        when(attributeCategoryMapper.selectById(1L)).thenReturn(existCategory);
        when(attributeCategoryMapper.selectOne(any())).thenReturn(null);

        Response response = attributeCategoryService.updateAttributeCategory(reqVO);

        Assertions.assertTrue(response.isSuccess());
        verify(attributeCategoryMapper, times(1)).updateById(any(PmsProductAttributeCategoryDO.class));
    }

    @Test
    void testUpdateAttributeCategory_NotFound() {
        PmsProductAttributeCategoryUpdateReqVO reqVO = new PmsProductAttributeCategoryUpdateReqVO();
        reqVO.setId(999L);
        reqVO.setName("更新后的分类名称");

        when(attributeCategoryMapper.selectById(999L)).thenReturn(null);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            attributeCategoryService.updateAttributeCategory(reqVO);
        });

        Assertions.assertEquals(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void testDeleteAttributeCategory_Success() {
        Long id = 1L;
        PmsProductAttributeCategoryDO categoryDO = new PmsProductAttributeCategoryDO();
        categoryDO.setId(1L);

        when(attributeCategoryMapper.selectById(id)).thenReturn(categoryDO);
        when(attributeMapper.selectCount(any())).thenReturn(0L);

        Response response = attributeCategoryService.deleteAttributeCategory(id);

        Assertions.assertTrue(response.isSuccess());
        verify(attributeCategoryMapper, times(1)).deleteById(id);
    }

    @Test
    void testDeleteAttributeCategory_InUse() {
        Long id = 1L;
        PmsProductAttributeCategoryDO categoryDO = new PmsProductAttributeCategoryDO();
        categoryDO.setId(1L);

        when(attributeCategoryMapper.selectById(id)).thenReturn(categoryDO);
        when(attributeMapper.selectCount(any())).thenReturn(5L);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            attributeCategoryService.deleteAttributeCategory(id);
        });

        Assertions.assertEquals(ResponseCodeEnum.ATTRIBUTE_IN_USE.getErrorCode(), exception.getErrorCode());
    }
}
